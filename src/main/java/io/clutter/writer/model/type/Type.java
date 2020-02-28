package io.clutter.writer.model.type;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class Type {

    public static final Type BYTE = primitiveType(byte.class, Byte.class);
    public static final Type SHORT = primitiveType(short.class, Short.class);
    public static final Type FLOAT = primitiveType(float.class, Float.class);
    public static final Type DOUBLE = primitiveType(double.class, Double.class);
    public static final Type CHAR = primitiveType(char.class, Character.class);
    public static final Type INT = primitiveType(int.class, Integer.class);
    public static final Type LONG = primitiveType(long.class, Long.class);
    public static final Type BOOLEAN = primitiveType(boolean.class, Boolean.class);
    public static final Type VOID = primitiveType(void.class, Void.class);
    public static final Type STRING = primitiveType(String.class, String.class);

    protected final String value;
    protected final String boxed;

    Type(String value, String boxed) {
        this.value = value;
        this.boxed = boxed;
    }

    private static Type primitiveType(Class<?> value, Class<?> boxed) {
        return new Type(value.getSimpleName(), boxed.getSimpleName());
    }

    public static Type raw(String rawType) {
        return new Type(rawType, rawType);
    }

    public static Type from(Class<?> type) {
        return new Type(type.getCanonicalName(), type.getSimpleName());
    }

    public static Type generic(Class<?> wrapper, Type type, Type... more) {
        return Type.raw(format("%s<%s%s>",
                wrapper.getCanonicalName(),
                type.boxed,
                more.length > 0 ? ", " + stream(more).map(t -> t.boxed).collect(joining(", ")) : "")
        );
    }

    public static Type listOf(Type type) {
        return generic(List.class, type);
    }

    public static Type setOf(Type type) {
        return generic(Set.class, type);
    }

    public static Type mapOf(Type keyType, Type valueType) {
        return generic(Map.class, keyType, valueType);
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
