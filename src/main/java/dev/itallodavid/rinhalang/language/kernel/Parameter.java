package dev.itallodavid.rinhalang.language.kernel;

public final class Parameter extends Node {
    private final String text;
    public Parameter(Location location, String text) {
        super(location);
        this.text = text;
    }

    public String text() {
        return text;
    }
}
