package io.clutter.model.z.model.value.array;

import io.clutter.model.z.collection.SafeList;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

final public class PrimitiveArrayValue implements ValueArray {

    private final SafeList<Object> values;

    private PrimitiveArrayValue(Object array) {
        this.values = new SafeList<>(IntStream
                .range(0, Array.getLength(array))
                .mapToObj(i -> Array.get(array, i))
                .collect(toList()));
    }

    public static ValueArray ofShortArray(short[] primitive) {
        return new PrimitiveArrayValue(primitive);
    }

    public static ValueArray ofIntArray(int[] primitive) {
        return new PrimitiveArrayValue(primitive);
    }

    public static ValueArray ofLongArray(long[] primitive) {
        return new PrimitiveArrayValue(primitive);
    }

    public static ValueArray ofFloatArray(float[] primitive) {
        return new PrimitiveArrayValue(primitive);
    }

    public static ValueArray ofDoubleArray(double[] primitive) {
        return new PrimitiveArrayValue(primitive);
    }

    public static ValueArray ofBoolArray(boolean[] primitive) {
        return new PrimitiveArrayValue(primitive);
    }

    public static ValueArray ofCharArray(char[] primitive) {
        return new PrimitiveArrayValue(primitive);
    }

    public static ValueArray ofByteArray(byte[] primitive) {
        return new PrimitiveArrayValue(primitive);
    }

    public ValueArray setValues(Consumer<List<Object>> mutation) {
        values.modify(mutation);
        return this;
    }

    public List<Object> getValues() {
        return values.getValues();
    }

    public <T> T accept(ValueArrayVisitor<T> visitor) {
        return visitor.visit(this);
    }
}