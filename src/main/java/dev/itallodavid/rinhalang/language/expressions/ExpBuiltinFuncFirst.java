package dev.itallodavid.rinhalang.language.expressions;

import dev.itallodavid.rinhalang.errors.InvalidTupleFunctionArgument;
import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Location;
import dev.itallodavid.rinhalang.language.kernel.Term;

public class ExpBuiltinFuncFirst extends ExpBuiltinFunction {
    public ExpBuiltinFuncFirst(Location location, Term value) {
        super(location, Kind.First, value);
    }

    @Override
    public Term perform(Term value) throws RuntimeException {
        if(value instanceof ExpTuple tuple) {
            return tuple.first();
        }

        throw new InvalidTupleFunctionArgument(this);
    }
}
