package io.clutter.model.z.model.ctor;

public interface ConstructorVisitor<T> {
    T visit(Constructor constructor);
}
