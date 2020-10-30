package io.clutter.model.type;

import java.util.Objects;

import static java.lang.String.format;

public class BoxedType implements Type {

    public static final BoxedType STRING = new BoxedType(String.class);
    private final Class<?> type;

    BoxedType(Class<?> type) {
        this.type = type;
    }

    public static BoxedType of(Class<?> type) {
        return type.isPrimitive() ? PrimitiveType.of(type).boxed() : new BoxedType(type);
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    public <T> T accept(TypeVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoxedType boxedType = (BoxedType) o;
        return type.equals(boxedType.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return format("BoxedType{type=%s}", type);
    }
}
