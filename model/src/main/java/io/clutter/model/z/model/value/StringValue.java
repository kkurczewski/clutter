package io.clutter.model.z.model.value;

final public class StringValue implements Value {

    private final String value;

    private StringValue(String value) {
        this.value = value;
    }

    public static Value of(String string) {
        return new StringValue(string);
    }

    public String getValue() {
        return value;
    }

    public <T> T accept(ValueVisitor<T> visitor) {
        return visitor.visit(this);
    }
}