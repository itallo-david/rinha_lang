package dev.itallodavid.rinhalang.language.expressions;

import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Location;
import dev.itallodavid.rinhalang.language.kernel.Parameter;
import dev.itallodavid.rinhalang.language.kernel.Term;

public class ExpLet extends Term {
    private final Parameter name;
    private final Term value;
    private final Term next;

    public ExpLet(Location location, Parameter name, Term value, Term next) {
        super(location, Kind.Let);
        this.name = name;
        this.value = value;
        this.next = next;
    }

    public Parameter name() {
        return name;
    }

    public Term value() {
        return value;
    }

    public Term next() {
        return next;
    }
}
