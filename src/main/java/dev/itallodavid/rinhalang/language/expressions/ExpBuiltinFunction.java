package dev.itallodavid.rinhalang.language.expressions;

import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Location;
import dev.itallodavid.rinhalang.language.kernel.Term;

public abstract class ExpBuiltinFunction extends Term {
    private final Term value;

    public ExpBuiltinFunction(Location location, Kind kind, Term value) {
        super(location, kind);
        this.value = value;
    }

    public Term value() {
        return value;
    }
    abstract public Term perform(Term value) throws RuntimeException;

    @Override
    public String toString() {
        return "<#closure>";
    }
}
