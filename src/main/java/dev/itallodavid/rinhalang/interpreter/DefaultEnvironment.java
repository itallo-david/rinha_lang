package dev.itallodavid.rinhalang.interpreter;

import dev.itallodavid.rinhalang.errors.UninitializedVariable;
import dev.itallodavid.rinhalang.language.kernel.Term;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultEnvironment implements Environment {
    private final Map<String, Term> environment;
    private final Environment parentEnvironment;

    public DefaultEnvironment() {
        this(null, new HashMap<>());
    }

    public DefaultEnvironment(Map<String, Term> environment) {
        this(null, environment);
    }

    public DefaultEnvironment(Environment parentEnvironment) {
        this(parentEnvironment, new HashMap<>());
    }

    public DefaultEnvironment(Environment parentEnvironment, Map<String, Term> environment) {
        this.parentEnvironment = parentEnvironment;
        this.environment = environment;
    }

    @Override
    public boolean contains(String key) {
        if(environment.containsKey(key)) return true;

        Environment env = parent();

        while(Objects.nonNull(env)) {
            if(env.environment().containsKey(key)) return true;
            env = env.parent();
        }

        return false;
    }

    @Override
    public void put(String key, Term value) {
        environment.put(key, value);
    }

    @Override
    public void put(Map<String, Term> scope) {
        environment.putAll(scope);
    }

    @Override
    public Term get(String key) {
        if(environment.containsKey(key)) return environment.get(key);

        Environment env = parent();

        while(Objects.nonNull(env)) {
            Term result = env.environment().get(key);
            if(Objects.nonNull(result)) return result;
            env = env.parent();
        }

        throw new UninitializedVariable(key) ;
    }

    @Override
    public Term remove(String key) {
        if(environment.containsKey(key)) return environment.remove(key);
        throw new UninitializedVariable(key);
    }

    @Override
    public Map<String, Term> environment() {
        return environment;
    }

    @Override
    public Environment parent() {
        return parentEnvironment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultEnvironment that = (DefaultEnvironment) o;
        return Objects.equals(environment, that.environment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(environment);
    }
}
