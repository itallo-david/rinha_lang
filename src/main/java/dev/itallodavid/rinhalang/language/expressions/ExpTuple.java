package dev.itallodavid.rinhalang.language.expressions;

import dev.itallodavid.rinhalang.language.kernel.Kind;
import dev.itallodavid.rinhalang.language.kernel.Location;
import dev.itallodavid.rinhalang.language.kernel.Term;
import dev.itallodavid.rinhalang.language.literals.LiteralString;

public class ExpTuple extends Term {
    private final Term first;
    private final Term second;

    public ExpTuple(Location location, Term first, Term second) {
        super(location, Kind.Tuple);
        this.first = first;
        this.second = second;
    }

    public Term first() {
        return first;
    }

    public Term second() {
        return second;
    }

    @Override
    public String toString() {
        String firstPrint = first.toString();
        String secondPrint = second.toString();

        if(first instanceof LiteralString)  firstPrint = String.format("\"%s\"", firstPrint);
        if(second instanceof LiteralString) secondPrint = String.format("\"%s\"", secondPrint);

        return String.format("(%s, %s)", firstPrint, secondPrint);
    }
}
