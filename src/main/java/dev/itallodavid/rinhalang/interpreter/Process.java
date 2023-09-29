package dev.itallodavid.rinhalang.interpreter;

import dev.itallodavid.rinhalang.language.kernel.Term;

public class Process {
    private final Term term;
    private final Environment environment;

    public Process(Term term, Environment environment) {
        this.term = term;
        this.environment = environment;
    }

    public Term term() {
        return term;
    }

    public Environment environment() {
        return environment;
    }
}
