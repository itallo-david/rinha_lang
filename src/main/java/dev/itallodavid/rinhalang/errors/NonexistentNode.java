package dev.itallodavid.rinhalang.errors;

public class NonexistentNode extends RuntimeException {
    public NonexistentNode() {
        super("There are no nodes to analyze.");
    }
}
