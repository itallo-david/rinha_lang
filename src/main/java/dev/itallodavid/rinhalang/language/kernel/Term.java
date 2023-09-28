package dev.itallodavid.rinhalang.language.kernel;

public abstract class Term extends Node {
    private final Kind kind;

    public Term(Location location, Kind kind) {
        super(location);
        this.kind = kind;
    }

    public Kind kind() {
        return kind;
    }
}
