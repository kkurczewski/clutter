package io.clutter.model.field;

public interface FieldVisitor<T> {
    T visit(Field field);
}
