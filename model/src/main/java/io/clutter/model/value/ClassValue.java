package io.clutter.model.value;

import io.clutter.model.type.Type;

final public class ClassValue implements Value {

    private final Class<?> value;

    private ClassValue(Class<?> value) {
        this.value = value;
    }

    public static Value of(Class<?> type) {
        return new ClassValue(type);
    }

    public static Value of(Type type) {
        return new ClassValue(type.getType());
    }

    public Class<?> getValue() {
        return value;
    }

    public <T> T accept(ValueVisitor<T> visitor) {
        return visitor.visit(this);
    }
}