package dev.itallodavid.rinhalang.language.literals;

import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Literal;
import dev.itallodavid.rinhalang.language.kernel.Location;

public class LiteralBoolean extends Literal<Boolean> {
    public LiteralBoolean(Location location, Boolean value) {
        super(location, Kind.Bool, value);
    }

    @Override
    public String toString() {
        return value() ? "true" : "false";
    }
}
