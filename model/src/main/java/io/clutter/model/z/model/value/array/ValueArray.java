package io.clutter.model.z.model.value.array;

public interface ValueArray {
    <T> T accept(ValueArrayVisitor<T> visitor);
}