package dev.itallodavid.rinhalang.language.expressions;

import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Location;
import dev.itallodavid.rinhalang.language.kernel.Term;

public class ExpIf extends Term {
    private final Term condition;
    private final Term then;
    private final Term otherwise;

    public ExpIf(Location location, Term condition, Term then, Term otherwise) {
        super(location, Kind.If);
        this.condition = condition;
        this.then = then;
        this.otherwise = otherwise;
    }

    public Term condition() {
        return condition;
    }

    public Term then() {
        return then;
    }

    public Term otherwise() {
        return otherwise;
    }
}
