package dev.itallodavid.rinhalang.language.literals;

import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Literal;
import dev.itallodavid.rinhalang.language.kernel.Location;

import java.math.BigInteger;

public class LiteralInteger extends Literal<BigInteger> {
    public LiteralInteger(Location location, BigInteger value) {
        super(location, Kind.Int, value);
    }
}
