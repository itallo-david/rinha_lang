package dev.itallodavid.rinhalang.language.literals;

import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Literal;
import dev.itallodavid.rinhalang.language.kernel.Location;

public class LiteralString extends Literal<String> {
    public LiteralString(Location location, String value) {
        super(location, Kind.Str, value);
    }
}
