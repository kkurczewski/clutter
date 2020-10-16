package io.clutter.model.z.model.value.array;

import io.clutter.model.z.collection.SafeList;

import java.util.List;
import java.util.function.Consumer;

final public class EnumArrayValue implements ValueArray {

    private final SafeList<Enum<?>> values;

    private EnumArrayValue(List<Enum<?>> values) {
        this.values = new SafeList<>(values);
    }

    public static ValueArray of(List<Enum<?>> values) {
        return new EnumArrayValue(values);
    }

    public ValueArray setValues(Consumer<List<Enum<?>>> mutation) {
        values.modify(mutation);
        return this;
    }

    public List<Enum<?>> getValues() {
        return values.getValues();
    }

    public <U> U accept(ValueArrayVisitor<U> visitor) {
        return visitor.visit(this);
    }
}