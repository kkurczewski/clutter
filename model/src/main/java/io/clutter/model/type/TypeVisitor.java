package io.clutter.model.type;

public interface TypeVisitor<T> {
    default T visit(ContainerType type) {
        return fallback(type);
    }

    default T visit(BoundedGenericType type) {
        return fallback(type);
    }

    default T visit(GenericType type) {
        return fallback(type);
    }

    default T visit(BoxedType type) {
        return fallback(type);
    }

    default T visit(DynamicType type) {
        return fallback(type);
    }

    default T visit(PrimitiveType type) {
        return fallback(type);
    }

    T fallback(Type type);
}