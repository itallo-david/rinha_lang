package dev.itallodavid.rinhalang.language.expressions;

import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Location;
import dev.itallodavid.rinhalang.language.kernel.Term;

import java.util.List;

public class ExpCall extends Term {
    private final Term callee;
    private final List<Term> arguments;

    public ExpCall(Location location, Term callee, List<Term> arguments) {
        super(location, Kind.Call);
        this.callee = callee;
        this.arguments = arguments;
    }

    public Term callee() {
        return callee;
    }

    public List<Term> arguments() {
        return arguments;
    }
}
