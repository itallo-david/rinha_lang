package dev.itallodavid.rinhalang.language.expressions;

import dev.itallodavid.rinhalang.errors.InvalidTupleFunctionArgument;
import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Location;
import dev.itallodavid.rinhalang.language.kernel.Term;

public class ExpBuiltinFuncSecond extends ExpBuiltinFunction {
    public ExpBuiltinFuncSecond(Location location, Term value) {
        super(location, Kind.Second, value);
    }

    @Override
    public Term perform(Term value) throws RuntimeException {
        if(value instanceof ExpTuple tuple) {
            return tuple.second();
        }

        throw new InvalidTupleFunctionArgument(this);
    }
}
