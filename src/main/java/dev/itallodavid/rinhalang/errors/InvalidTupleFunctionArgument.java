package dev.itallodavid.rinhalang.errors;

import dev.itallodavid.rinhalang.language.expressions.ExpBuiltinFunction;

public class InvalidTupleFunctionArgument extends RuntimeException {
    public InvalidTupleFunctionArgument(ExpBuiltinFunction function) {
        super(String.format("Function: %s - received invalid values.", function.kind().toString()));
    }
}