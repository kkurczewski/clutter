package io.clutter.model.z.model.value;

public interface Value {
    <T> T accept(ValueVisitor<T> visitor);
}