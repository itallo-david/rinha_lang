package dev.itallodavid.rinhalang.language.expressions;

import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Location;
import dev.itallodavid.rinhalang.language.kernel.Term;

public class ExpBuiltinFuncPrint extends ExpBuiltinFunction{
    public ExpBuiltinFuncPrint(Location location, Term value) {
        super(location, Kind.Print, value);
    }

    @Override
    public Term perform(Term value) throws RuntimeException {
        System.out.println(value);
        return value;
    }
}
