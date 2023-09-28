package dev.itallodavid.rinhalang.language.kernel;

public enum BinaryOperator {
    Add("+"),
    Sub("-"),
    Mul("*"),
    Div("/"),
    Rem("%"),
    Eq("=="),
    Neq("!="),
    Lt("<"),
    Gt(">"),
    Lte("<="),
    Gte(">="),
    And("&&"),
    Or("||");

    private final String symbol;

    BinaryOperator(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() { return symbol; }
}
