package io.clutter.writer.model.param;

import io.clutter.writer.model.type.Type;

import java.util.Objects;

import static java.lang.String.valueOf;

final public class Param {

    private final String name;
    private final String value;

    private Param(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static Param of(String name, Type value) {
        return new Param(name, valueOf(value));
    }

    public static Param raw(String name, String value) {
        return new Param(name, value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Param param = (Param) o;
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
