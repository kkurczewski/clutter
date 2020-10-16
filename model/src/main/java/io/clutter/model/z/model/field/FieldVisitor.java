package io.clutter.model.z.model.field;

public interface FieldVisitor<T> {
    T visit(Field field);
}
