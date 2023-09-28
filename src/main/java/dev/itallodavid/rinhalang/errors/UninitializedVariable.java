package dev.itallodavid.rinhalang.errors;

public class UninitializedVariable extends RuntimeException {
    public UninitializedVariable(String variableName) {
        super(String.format("The variable was not initialized: %s", variableName));
    }
}
