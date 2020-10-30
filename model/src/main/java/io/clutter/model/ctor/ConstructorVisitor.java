package io.clutter.model.ctor;

public interface ConstructorVisitor<T> {
    T visit(Constructor constructor);
}
