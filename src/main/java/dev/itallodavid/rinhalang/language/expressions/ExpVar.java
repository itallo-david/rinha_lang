package dev.itallodavid.rinhalang.language.expressions;

import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Location;
import dev.itallodavid.rinhalang.language.kernel.Term;

public class ExpVar extends Term {
    private final String text;

    public ExpVar(Location location, String text) {
        super(location, Kind.Var);
        this.text = text;
    }

    public String text() {
        return text;
    }
}
