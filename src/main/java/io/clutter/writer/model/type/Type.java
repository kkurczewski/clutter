package io.clutter.writer.model.type;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;

final public class Type {

    public static final Type BYTE = new Type(byte.class, Byte.class);
    public static final Type SHORT = new Type(short.class, Short.class);
    public static final Type FLOAT = new Type(float.class, Float.class);
    public static final Type DOUBLE = new Type(double.class, Double.class);
    public static final Type CHAR = new Type(char.class, Character.class);
    public static final Type INT = new Type(int.class, Integer.class);
    public static final Type LONG = new Type(long.class, Long.class);
    public static final Type BOOLEAN = new Type(boolean.class, Boolean.class);
    public static final Type VOID = new Type(void.class, Void.class);

    public static final Type STRING = new Type(String.class.getSimpleName());

    private final String value;
    private final String boxed;

    private Type(String value, String boxed) {
        this.value = value;
        this.boxed = boxed;
    }

    private Type(Class<?> value, Class<?> boxed) {
        this(value.getSimpleName(), boxed.getSimpleName());
    }

    private Type(String value) {
        this(value, value);
    }

    public static Type raw(String rawType) {
        return new Type(rawType, rawType);
    }

    public static Type listOf(Type type) {
        return new Type(format("%s<%s>", List.class.getCanonicalName(), type.boxed));
    }

    public static Type setOf(Type type) {
        return new Type(format("%s<%s>", Set.class.getCanonicalName(), type.boxed));
    }

    public static Type mapOf(Type keyType, Type valueType) {
        return new Type(format("%s<%s, %s>", Map.class.getCanonicalName(), keyType.boxed, valueType.boxed));
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
