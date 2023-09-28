package dev.itallodavid.rinhalang.language.expressions;

import dev.itallodavid.rinhalang.language.kernel.BinaryOperator;
import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Location;
import dev.itallodavid.rinhalang.language.kernel.Term;

public class ExpBinary extends Term {
    private final Term lhs;
    private final BinaryOperator op;
    private final Term rhs;

    public ExpBinary(Location location, Term lhs, BinaryOperator op, Term rhs) {
        super(location, Kind.Binary);
        this.lhs = lhs;
        this.op = op;
        this.rhs = rhs;
    }

    public Term lhs() {
        return lhs;
    }

    public BinaryOperator op() {
        return op;
    }

    public Term rhs() {
        return rhs;
    }

    @Override
    public String toString() {
        return String.format("Exp. Binary: <%s> %s <%s>", lhs.kind(), op, rhs.kind());
    }
}
