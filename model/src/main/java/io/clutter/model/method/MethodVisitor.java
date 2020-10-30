package io.clutter.model.method;

public interface MethodVisitor<T> {
    T visit(Method method);
}
