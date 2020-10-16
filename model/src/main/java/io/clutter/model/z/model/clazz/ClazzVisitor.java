package io.clutter.model.z.model.clazz;

public interface ClazzVisitor<T> {
    T visit(Clazz clazz);
}
