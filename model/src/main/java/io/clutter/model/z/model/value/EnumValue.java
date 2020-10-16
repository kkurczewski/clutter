package io.clutter.model.z.model.value;

final public class EnumValue implements Value {

    private final Enum<?> value;

    private EnumValue(Enum<?> value) {
        this.value = value;
    }

    public static Value of(Enum<?> type) {
        return new EnumValue(type);
    }

    public Enum<?> getValue() {
        return value;
    }

    public <T> T accept(ValueVisitor<T> visitor) {
        return visitor.visit(this);
    }
}