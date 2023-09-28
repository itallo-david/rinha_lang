package dev.itallodavid.rinhalang.language.kernel;

import java.util.Objects;

public abstract class Literal<T> extends Term {
    private final T value;

    public Literal(Location location, Kind kind, T value) {
        super(location, kind);
        this.value = value;
    }

    public T value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Literal<?>)) return false;
        Literal<?> literal = (Literal<?>) o;
        return Objects.equals(value, literal.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
