package io.clutter.model.type;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.String.format;

final public class PrimitiveType implements Type {

    public static final PrimitiveType SHORT = new PrimitiveType(short.class, Short.class);
    public static final PrimitiveType INT = new PrimitiveType(int.class, Integer.class);
    public static final PrimitiveType LONG = new PrimitiveType(long.class, Long.class);
    public static final PrimitiveType FLOAT = new PrimitiveType(float.class, Float.class);
    public static final PrimitiveType DOUBLE = new PrimitiveType(double.class, Double.class);
    public static final PrimitiveType BYTE = new PrimitiveType(byte.class, Byte.class);
    public static final PrimitiveType CHAR = new PrimitiveType(char.class, Character.class);
    public static final PrimitiveType BOOLEAN = new PrimitiveType(boolean.class, Boolean.class);
    public static final PrimitiveType VOID = new PrimitiveType(void.class, Void.class);

    private final Class<?> type;
    private final Class<?> boxed;

    private PrimitiveType(Class<?> type, Class<?> boxed) {
        this.type = type;
        this.boxed = boxed;
    }

    public static PrimitiveType of(Class<?> type) {
        return Stream.of(SHORT, INT, LONG, FLOAT, DOUBLE, BYTE, CHAR, BOOLEAN, VOID)
                .filter(primitiveType -> primitiveType
                        .getType()
                        .equals(type)
                )
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(type.getCanonicalName()));
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    public BoxedType boxed() {
        return BoxedType.of(boxed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimitiveType that = (PrimitiveType) o;
        return type.equals(that.type) && Objects.equals(boxed, that.boxed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), boxed);
    }

    @Override
    public String toString() {
        return format("PrimitiveType{type=%s, boxed=%s}", type, boxed);
    }
}
