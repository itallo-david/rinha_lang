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
    private final Map<String, Environment> scope = new HashMap<>();

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
        if (term instanceof Literal<?>) return new Process(term, env);
        else if (term instanceof ExpTuple expTuple) {
            Term first = expTuple.first();
            Term second = expTuple.second();

            if (first instanceof ExpBuiltinFunction) first = eval(first, env).term();
            if (second instanceof ExpBuiltinFunction) second = eval(second, env).term();

            return new Process(new ExpTuple(expTuple.location(), first, second), env);
        }
        else if (term instanceof ExpBuiltinFunction builtinFunction) {
            Process process = eval(builtinFunction.value(), env);
            Term value = process.term();
            return new Process(builtinFunction.perform(value), process.environment());
        }
        else if (term instanceof ExpBinary expBinary) {
            Term lhs = expBinary.lhs();
            Term rhs = expBinary.rhs();

            while ((lhs instanceof ExpCall || lhs instanceof ExpVar || lhs instanceof ExpBinary || lhs instanceof ExpBuiltinFunction))
                lhs = eval(lhs, env, true).term();
            while ((rhs instanceof ExpCall || rhs instanceof ExpVar || rhs instanceof ExpBinary || rhs instanceof ExpBuiltinFunction))
                rhs = eval(rhs, env, true).term();

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
        else if (term instanceof ExpLet expLet) {
            Parameter name = expLet.name();
            Term value = expLet.value();

            if (value instanceof ExpCall expCall) {
                Environment scope = new DefaultEnvironment(env);
                Process process = eval(expCall.callee(), env);
                Term callee = process.term();

                if(callee instanceof ExpDefFunction function) {
                    List<Parameter> parameters = function.parameters();
                    List<Term> arguments = expCall.arguments();

                    if (parameters.size() != arguments.size())
                        throw new InvalidNumberOfFunctionParameters(
                                ((ExpVar) expCall.callee()).text(), parameters.size(), arguments.size());
                    for (int index = 0; index < parameters.size(); index++) {
                        String key = parameters.get(index).text();
                        Term arg = arguments.get(index);

                        while (!(arg instanceof Literal<?> || arg instanceof ExpTuple)) {
                            arg = eval(arg, scope, true).term();
                        }

                        scope.put(key, arg);
                    }

                    Process process1 = eval(function.value(), scope);
                    scope.put(process1.environment().environment());
                    this.scope.put(name.text(), scope);
                }
            }

            if (!(value instanceof ExpDefFunction)) value = eval(value, env).term();

            env.put(name.text(), value);
            return withoutEval ? new Process(expLet.next(), env) : eval(expLet.next(), env);
        }
        else if (term instanceof ExpVar expVar)  return new Process(env.get(expVar.text()), env);
        else if (term instanceof ExpDefFunction expDefFunction) return new Process(expDefFunction.value(), env);
        else if (term instanceof ExpCall expCall) {
            if(expCall.callee() instanceof ExpVar expVar && this.scope.containsKey(expVar.text())) {
                env = new DefaultEnvironment(env);
                env.put(scope.get(expVar.text()).environment());
            }

            Term callee = eval(expCall.callee(), env).term();

            if(callee instanceof ExpDefFunction function) {
                List<Term> arguments = expCall.arguments();
                Environment newScope = new DefaultEnvironment(env);

                List<Parameter> parameters = function.parameters();

                if (parameters.size() != arguments.size()) throw new InvalidNumberOfFunctionParameters(
                        ((ExpVar) expCall.callee()).text(),
                        parameters.size(),
                        arguments.size());
                for (int index = 0; index < parameters.size(); index++) {
                    String key = parameters.get(index).text();
                    Term value = arguments.get(index);

                    while (!(value instanceof Literal<?> || value instanceof ExpTuple)) {
                        value = eval(value, newScope, true).term();
                    }

                    newScope.put(key, value);
                }

                if (!functionCallCache.containsKey(callee)) functionCallCache.put(callee, new HashMap<>());

                if (functionCallCache.get(callee).containsKey(newScope.hashCode()))
                    term = functionCallCache.get(callee).get(newScope.hashCode());
                else {
                    term = eval(callee, newScope).term();

                    while (!(term instanceof Literal<?> || term instanceof ExpTuple || term instanceof ExpDefFunction)) {
                        Process process = eval(term, newScope, true);
                        term = process.term();
                        newScope.put(process.environment().environment());
                    }

                    functionCallCache.get(callee).put(newScope.hashCode(), term);
                    env = newScope;
                }
            }
            else
                term = eval(callee, env).term();

            return (
                    term instanceof ExpDefFunction || term instanceof Literal<?> || term instanceof ExpTuple
            ) ? new Process(term, env) : eval(term, env);
        }
        else if (term instanceof ExpIf expIf) {
            Term condition = eval(expIf.condition(), env).term();
            Term then = expIf.then();
            Term otherwise = expIf.otherwise();

            if (condition instanceof LiteralBoolean literalBoolean && literalBoolean.value())
                return withoutEval ? new Process(then, env) : eval(then, env);
            else
                return withoutEval ? new Process(otherwise, env) : eval(otherwise, env);
        }

        return eval(term, env);
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