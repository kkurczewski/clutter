package io.clutter.model.param;

import io.clutter.model.type.Type;

import java.util.Objects;

final public class Argument {

    private final String name;
    private final Type value;

    public Argument(String name, Type value) {
        this.name = name;
        this.value = value;
    }

    public Argument(String name, Class<?> type) {
        this(name, Type.of(type));
    }

    public String getName() {
        return name;
    }

    public Type getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Argument param = (Argument) o;
        return name.equals(param.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + '{' + value + '}';
    }
}
