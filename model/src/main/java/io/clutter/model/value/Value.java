package io.clutter.model.value;

public interface Value {
    <T> T accept(ValueVisitor<T> visitor);
}