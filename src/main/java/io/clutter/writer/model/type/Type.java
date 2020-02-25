package io.clutter.writer.model.type;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;

public class Type {

    public static final Type LONG = new Type(long.class.getSimpleName());
    public static final Type INT = new Type(int.class.getSimpleName());
    public static final Type BOOLEAN = new Type(boolean.class.getSimpleName());
    public static final Type STRING = new Type(String.class.getSimpleName());
    public static final Type VOID = new Type(void.class.getSimpleName());

    private final String value;

    private Type(String value) {
        this.value = value;
    }

    public static Type raw(String rawType) {
        return new Type(rawType);
    }

    public static Type listOf(Type type) {
        return new Type(format("%s<%s>", List.class.getCanonicalName(), type.value));
    }

    public static Type setOf(Type type) {
        return new Type(format("%s<%s>", Set.class.getCanonicalName(), type.value));
    }

    public static Type mapOf(Type keyType, Type valueType) {
        return new Type(format("%s<%s, %s>", Map.class.getCanonicalName(), keyType.value, valueType.value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return value.equals(type.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
