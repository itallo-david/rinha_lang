package dev.itallodavid.rinhalang.language.kernel;

public abstract class Node {
    private final Location location;

    public Node(Location location) {
        this.location = location;
    }

    public Location location() {
        return location;
    }
}
