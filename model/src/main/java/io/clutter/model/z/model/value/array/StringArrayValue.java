package io.clutter.model.z.model.value.array;

import io.clutter.model.z.collection.SafeList;

import java.util.List;
import java.util.function.Consumer;

final public class StringArrayValue implements ValueArray {

    private final SafeList<String> values;

    private StringArrayValue(List<String> values) {
        this.values = new SafeList<>(values);
    }

    public static ValueArray of(List<String> value) {
        return new StringArrayValue(value);
    }

    public ValueArray setValues(Consumer<List<String>> mutation) {
        values.modify(mutation);
        return this;
    }

    public List<String> getValues() {
        return values.getValues();
    }

    public <T> T accept(ValueArrayVisitor<T> visitor) {
        return visitor.visit(this);
    }
}