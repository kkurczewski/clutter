package io.clutter.writer.model.param;

import io.clutter.writer.model.type.Type;

import java.util.Objects;

import static java.lang.String.valueOf;

final public class Param {

    private final String name;
    private final String value;

    public Param(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static Param of(String name, Type value) {
        return new Param(name, valueOf(value));
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
        return name.equals(param.name) && value.equals(param.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
