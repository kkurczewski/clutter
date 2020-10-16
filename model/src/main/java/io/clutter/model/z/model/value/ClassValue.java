package io.clutter.model.z.model.value;

final public class ClassValue implements Value {

    private final Class<?> value;

    private ClassValue(Class<?> value) {
        this.value = value;
    }

    public static Value of(Class<?> type) {
        return new ClassValue(type);
    }

    public Class<?> getValue() {
        return value;
    }

    public <T> T accept(ValueVisitor<T> visitor) {
        return visitor.visit(this);
    }
}