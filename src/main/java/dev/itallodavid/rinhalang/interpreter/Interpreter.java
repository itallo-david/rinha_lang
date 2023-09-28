package dev.itallodavid.rinhalang.interpreter;

import dev.itallodavid.rinhalang.errors.NonexistentNode;
import dev.itallodavid.rinhalang.language.kernel.Node;
import dev.itallodavid.rinhalang.language.kernel.Term;

public interface Interpreter {
    public Node file();
    public Environment environment();
    public Term perform() throws NonexistentNode;
}
