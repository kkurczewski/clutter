package io.clutter.model.type;

public interface Type {

    /**
     * Creates {@link PrimitiveType} if passed primitive, {@link BoxedType} otherwise
     */
    static Type of(Class<?> type) {
        return type.isPrimitive() ? PrimitiveType.of(type) : new BoxedType(type);
    }

    Class<?> getType();

    <T> T accept(TypeVisitor<T> type);
}
