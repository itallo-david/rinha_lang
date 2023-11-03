package dev.itallodavid.rinhalang.interpreter;

import dev.itallodavid.rinhalang.errors.InvalidBinaryOperation;
import dev.itallodavid.rinhalang.errors.InvalidNumberOfFunctionParameters;
import dev.itallodavid.rinhalang.errors.NonexistentNode;
import dev.itallodavid.rinhalang.language.expressions.*;
import dev.itallodavid.rinhalang.language.kernel.*;
import dev.itallodavid.rinhalang.language.literals.LiteralBoolean;
import dev.itallodavid.rinhalang.language.literals.LiteralInteger;
import dev.itallodavid.rinhalang.language.literals.LiteralString;

import java.math.BigInteger;
import java.util.*;

public class RinhaLangInterpreter implements Interpreter {
    private final File node;
    private final Environment environment;
    private final Map<Term, Map<Integer, Term>> functionCallCache = new WeakHashMap<>();
    private final Map<Term, UUID> scopes = new HashMap<>();
    private final Map<UUID, Environment> scopeEnvironment = new HashMap<>();

    public RinhaLangInterpreter(File node) {
        this(node, new DefaultEnvironment());
    }

    public RinhaLangInterpreter(File node, Environment environment) {
        if (Objects.isNull(node)) throw new NonexistentNode();
        this.node = node;
        this.environment = Objects.isNull(environment) ? new DefaultEnvironment() : environment;
    }

    private Term eval(File file, Environment env) {
        return eval(file.expression(), env, false).term();
    }

    private Process eval(Term term, Environment env) {
        return eval(term, env, false);
    }

    private Process eval(Term term, Environment env, boolean withoutEval) {
        if (term instanceof Literal<?> || term instanceof ExpDefFunction) return new Process(term, env);
        else if (term instanceof ExpTuple expTuple) {
            Term first = eval(expTuple.first(), env).term();
            Term second = eval(expTuple.second(), env).term();
            return new Process(new ExpTuple(expTuple.location(), first, second), env);
        }
        else if (term instanceof ExpBuiltinFunction builtinFunction) {
            Process process = eval(builtinFunction.value(), env);
            return new Process(builtinFunction.perform(process.term()), process.environment());
        }
        else if (term instanceof ExpVar expVar) {
            String varName = expVar.text();
            Term varValue = env.get(varName);

            if(varValue instanceof ExpDefFunction) {
                UUID scope = scopes.get(varValue);
                Environment environment = scopeEnvironment.get(scope);
                return new Process(varValue, environment);
            }

            return new Process(varValue, env);
        }
        else if (term instanceof ExpLet expLet) {
            Parameter name = expLet.name();
            Process process = eval(expLet.value(), env);
            Term value = process.term();

            if(value instanceof ExpDefFunction) {
                Environment finalEnv = new DefaultEnvironment(env.parent());
                UUID scope = randomUUID();
                finalEnv.put(env.environment());
                scopes.put(value, scope);
                scopeEnvironment.put(scope, finalEnv);
            }

            env = process.environment();
            env.put(name.text(), value);

            return withoutEval ? new Process(expLet.next(), env) : eval(expLet.next(), env);
        }
        else if (term instanceof ExpIf expIf) {
            Process process  = eval(expIf.condition(), env);
            env = process.environment();
            Term condition = process.term();
            Term then = expIf.then();
            Term otherwise = expIf.otherwise();

            if (condition instanceof LiteralBoolean literalBoolean && literalBoolean.value())
                return withoutEval ? new Process(then, env) : eval(then, env);
            else
                return withoutEval ? new Process(otherwise, env) : eval(otherwise, env);
        }
        else if (term instanceof ExpCall expCall) {
            Environment newScope = new DefaultEnvironment(env);
            Process firstProcess = eval(expCall.callee(), env);
            Term callee = firstProcess.term();

            if(callee instanceof ExpDefFunction function) {
                List<Term> arguments = expCall.arguments();
                List<Parameter> parameters = function.parameters();

                if(parameters.size() != arguments.size()) {
                    boolean calleeIsVar = expCall.callee() instanceof ExpVar;

                    throw new InvalidNumberOfFunctionParameters(
                            (calleeIsVar ? ((ExpVar) expCall.callee()).text(): expCall.callee().toString()),
                            parameters.size(),
                            arguments.size());
                }

                Process argumentProcess = null;

                for(int index = 0; index < parameters.size(); index++) {
                    String argumentName = parameters.get(index).text();
                    Term argumentValue = arguments.get(index);

                    while(!(argumentValue instanceof Literal<?> || argumentValue instanceof ExpTuple || argumentValue instanceof ExpDefFunction)) {
                        argumentProcess = eval(argumentValue, newScope);
                        argumentValue = argumentProcess.term();
                        newScope = argumentProcess.environment();
                    }

                    newScope.put(argumentName, argumentValue);
                }

                return eval(function.value(), newScope);
            }

            return eval(callee, newScope);
        }
        else if (term instanceof ExpBinary expBinary) {
            Term lhs = expBinary.lhs();
            Term rhs = expBinary.rhs();

            while ((lhs instanceof ExpCall || lhs instanceof ExpVar || lhs instanceof ExpBinary || lhs instanceof ExpBuiltinFunction)) {
                Process process = eval(lhs, env, true);
                lhs = process.term();
                env = process.environment();
            }
            while ((rhs instanceof ExpCall || rhs instanceof ExpVar || rhs instanceof ExpBinary || rhs instanceof ExpBuiltinFunction)) {
                Process process = eval(rhs, env, true);
                rhs = process.term();
                env = process.environment();
            }

            BinaryOperator operator = expBinary.op();
            RuntimeException exception = new InvalidBinaryOperation(operator, lhs.kind(), rhs.kind());

            switch (operator) {
                case Add, Sub, Mul, Div, Rem, Lt, Gt, Lte, Gte -> {
                    if (!(lhs instanceof Literal<?>) || !(rhs instanceof Literal<?>))
                        throw exception;
                }
            }

            return switch (operator) {
                case Add -> {
                    if (lhs instanceof LiteralInteger && rhs instanceof LiteralInteger)
                        yield new Process(new LiteralInteger(
                                null,
                                ((LiteralInteger) lhs).value().add(((LiteralInteger) rhs).value())), env);
                    yield new Process(new LiteralString(
                            null,
                            String.format("%s%s", ((Literal<?>) lhs).value(), ((Literal<?>) rhs).value())), env);
                }
                case Sub -> {
                    if (lhs instanceof LiteralInteger && rhs instanceof LiteralInteger)
                        yield new Process(new LiteralInteger(
                                null,
                                ((LiteralInteger) lhs).value().subtract(((LiteralInteger) rhs).value())), env);
                    throw exception;
                }
                case Mul -> {
                    if (lhs instanceof LiteralInteger && rhs instanceof LiteralInteger)
                        yield new Process(new LiteralInteger(
                                null,
                                ((LiteralInteger) lhs).value().multiply(((LiteralInteger) rhs).value())), env);
                    throw exception;
                }
                case Div -> {
                    if (lhs instanceof LiteralInteger && rhs instanceof LiteralInteger)
                        yield new Process(new LiteralInteger(
                                null,
                                ((LiteralInteger) lhs).value().divide(((LiteralInteger) rhs).value())), env);
                    throw exception;
                }
                case Rem -> {
                    if (lhs instanceof LiteralInteger && rhs instanceof LiteralInteger)
                        yield new Process(new LiteralInteger(
                                null,
                                ((LiteralInteger) lhs).value().mod(((LiteralInteger) rhs).value())), env);
                    throw exception;
                }

                case Eq -> new Process(new LiteralBoolean(null, (lhs.equals(rhs) ? Boolean.TRUE : Boolean.FALSE)), env);
                case Neq -> new Process(new LiteralBoolean(null, (!lhs.equals(rhs) ? Boolean.TRUE : Boolean.FALSE)), env);
                case Lt -> {
                    boolean result = false;

                    if (lhs instanceof LiteralString lhsString) {
                        String rhsValue = ((Literal<?>) rhs).value().toString();
                        result = lhsString.value().length() < rhsValue.length();
                    } else if (lhs instanceof LiteralInteger lhsInteger) {
                        BigInteger rhsValue = null;

                        if (rhs instanceof LiteralInteger rhsInteger) rhsValue = rhsInteger.value();
                        else if (rhs instanceof LiteralString rhsString) {
                            try {
                                rhsValue = new BigInteger(rhsString.value());
                            } catch (NumberFormatException formatException) {
                                rhsValue = lhsInteger.value();
                            }
                        } else if (rhs instanceof LiteralBoolean rhsBool) {
                            rhsValue = new BigInteger(rhsBool.value() ? "1" : "0");
                        }

                        result = lhsInteger.value().compareTo(rhsValue) < 0;
                    }
                    yield new Process(new LiteralBoolean(
                            null,
                            result), env);
                }
                case Lte -> {
                    boolean result = false;

                    if (lhs instanceof LiteralString lhsString) {
                        String rhsValue = ((Literal<?>) rhs).value().toString();
                        result = lhsString.value().length() <= rhsValue.length();
                    } else if (lhs instanceof LiteralInteger lhsInteger) {
                        BigInteger rhsValue = null;

                        if (rhs instanceof LiteralInteger rhsInteger) rhsValue = rhsInteger.value();
                        else if (rhs instanceof LiteralString rhsString) {
                            try {
                                rhsValue = new BigInteger(rhsString.value());
                            } catch (NumberFormatException formatException) {
                                rhsValue = lhsInteger.value();
                            }
                        } else if (rhs instanceof LiteralBoolean rhsBool) {
                            rhsValue = new BigInteger(rhsBool.value() ? "1" : "0");
                        }

                        result =
                                lhsInteger.value().compareTo(rhsValue) < 0 || lhsInteger.value().compareTo(rhsValue) == 0;
                    }
                    yield new Process(new LiteralBoolean(
                            null,
                            result), env);
                }
                case Gt -> {
                    boolean result = false;

                    if (lhs instanceof LiteralString lhsString) {
                        String rhsValue = ((Literal<?>) rhs).value().toString();
                        result = lhsString.value().length() > rhsValue.length();
                    } else if (lhs instanceof LiteralInteger lhsInteger) {
                        BigInteger rhsValue = null;

                        if (rhs instanceof LiteralInteger rhsInteger) rhsValue = rhsInteger.value();
                        else if (rhs instanceof LiteralString rhsString) {
                            try {
                                rhsValue = new BigInteger(rhsString.value());
                            } catch (NumberFormatException formatException) {
                                rhsValue = lhsInteger.value();
                            }
                        } else if (rhs instanceof LiteralBoolean rhsBool) {
                            rhsValue = new BigInteger(rhsBool.value() ? "1" : "0");
                        }

                        result = lhsInteger.value().compareTo(rhsValue) > 0;
                    }

                    yield new Process(new LiteralBoolean(null, result), env);
                }
                case Gte -> {
                    boolean result = false;

                    if (lhs instanceof LiteralString lhsString) {
                        String rhsValue = ((Literal<?>) rhs).value().toString();
                        result = lhsString.value().length() >= rhsValue.length();
                    } else if (lhs instanceof LiteralInteger lhsInteger) {
                        BigInteger rhsValue = null;

                        if (rhs instanceof LiteralInteger rhsInteger) rhsValue = rhsInteger.value();
                        else if (rhs instanceof LiteralString rhsString) {
                            try {
                                rhsValue = new BigInteger(rhsString.value());
                            } catch (NumberFormatException formatException) {
                                rhsValue = lhsInteger.value();
                            }
                        } else if (rhs instanceof LiteralBoolean rhsBool) {
                            rhsValue = new BigInteger(rhsBool.value() ? "1" : "0");
                        }

                        result = lhsInteger.value().compareTo(rhsValue) >= 0;
                    }

                    yield new Process(new LiteralBoolean(null, result), env);
                }
                case Or -> {
                    boolean result = false;

                    if (lhs instanceof Literal<?> literal) {
                        if (literal instanceof LiteralBoolean literalBoolean) result = literalBoolean.value();
                        else if (literal instanceof LiteralString literalString)
                            result = !literalString.value().isEmpty();
                        else if (literal instanceof LiteralInteger literalInteger)
                            result = literalInteger.value().compareTo(new BigInteger("0")) > 0;
                        else result = true;
                    }

                    if (!result && rhs instanceof Literal<?> literal) {
                        if (literal instanceof LiteralBoolean literalBoolean) result = literalBoolean.value();
                        else if (literal instanceof LiteralString literalString)
                            result = !literalString.value().isEmpty();
                        else if (literal instanceof LiteralInteger literalInteger)
                            result = literalInteger.value().compareTo(new BigInteger("0")) > 0;
                        else result = true;
                    }

                    yield new Process(new LiteralBoolean(null, result), env);
                }
                case And -> {
                    boolean result = false;

                    if (lhs instanceof Literal<?> literal) {
                        if (literal instanceof LiteralBoolean literalBoolean) result = literalBoolean.value();
                        else if (literal instanceof LiteralString literalString)
                            result = !literalString.value().isEmpty();
                        else if (literal instanceof LiteralInteger literalInteger)
                            result = literalInteger.value().compareTo(new BigInteger("0")) > 0;
                        else result = true;
                    }

                    if (result && rhs instanceof Literal<?> literal) {
                        if (literal instanceof LiteralBoolean literalBoolean) result = literalBoolean.value();
                        else if (literal instanceof LiteralString literalString)
                            result = !literalString.value().isEmpty();
                        else if (literal instanceof LiteralInteger literalInteger)
                            result = literalInteger.value().compareTo(new BigInteger("0")) > 0;
                    } else result = false;

                    yield new Process(new LiteralBoolean(null, result), env);
                }
            };
        }

        return eval(term, env);
    }

    private UUID randomUUID() {
        return UUID.randomUUID();
    }

    @Override
    public File file() {
        return node;
    }

    @Override
    public Environment environment() {
        return environment;
    }

    @Override
    public Term perform() {
        return eval(file(), environment());
    }
}