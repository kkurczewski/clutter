package io.clutter.model.z.model.type;

public interface TypeVisitor<T> {
    T visit(ContainerType type);
    T visit(BoundedGenericType type);
    T visit(GenericType type);
    T visit(BoxedType type);
    T visit(DynamicType type);
    T visit(Type type);
}