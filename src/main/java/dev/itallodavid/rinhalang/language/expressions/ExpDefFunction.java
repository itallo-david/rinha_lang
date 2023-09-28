package dev.itallodavid.rinhalang.language.expressions;

import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Location;
import dev.itallodavid.rinhalang.language.kernel.Parameter;
import dev.itallodavid.rinhalang.language.kernel.Term;

import java.util.List;

public class ExpDefFunction extends Term  {
    private final List<Parameter> parameters;
    private final Term value;

    public ExpDefFunction(Location location, List<Parameter> parameters, Term value) {
        super(location, Kind.Function);
        this.parameters = parameters;
        this.value = value;
    }

    public List<Parameter> parameters() {
        return parameters;
    }

    public Term value() {
        return value;
    }

    @Override
    public String toString() {
        return "<#closure>";
    }
}
