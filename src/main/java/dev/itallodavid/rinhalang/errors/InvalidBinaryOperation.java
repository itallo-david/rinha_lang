package dev.itallodavid.rinhalang.errors;

import dev.itallodavid.rinhalang.language.kernel.BinaryOperator;
import dev.itallodavid.rinhalang.language.kernel.Kind;

public class InvalidBinaryOperation extends RuntimeException {
    public InvalidBinaryOperation(BinaryOperator op, Kind lhsKind, Kind rhsKind) {
        super(String.format(
                "Binary operation <%s> %s <%s> is invalid.",
                lhsKind.toString(),
                op.symbol(),
                rhsKind.toString()));
    }
}