package io.clutter.model.z.model.value;

final public class PrimitiveValue implements Value {

    private final Object value;

    private PrimitiveValue(Object value) {
        this.value = value;
    }

    public static Value of(String string) {
        return new PrimitiveValue(string);
    }

    public static Value ofInt(int primitive) {
        return new PrimitiveValue(primitive);
    }

    public static Value ofLong(long primitive) {
        return new PrimitiveValue(primitive);
    }

    public static Value ofFloat(float primitive) {
        return new PrimitiveValue(primitive);
    }

    public static Value ofDouble(double primitive) {
        return new PrimitiveValue(primitive);
    }

    public static Value ofBool(boolean primitive) {
        return new PrimitiveValue(primitive);
    }

    public static Value ofChar(char primitive) {
        return new PrimitiveValue(primitive);
    }

    public Object getValue() {
        return value;
    }

    public <T> T accept(ValueVisitor<T> visitor) {
        return visitor.visit(this);
    }
}