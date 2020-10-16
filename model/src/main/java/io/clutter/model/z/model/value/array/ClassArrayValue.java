package io.clutter.model.z.model.value.array;

import io.clutter.model.z.collection.SafeList;

import java.util.List;
import java.util.function.Consumer;

final public class ClassArrayValue implements ValueArray {

    private final SafeList<Class<?>> values;

    private ClassArrayValue(List<Class<?>> values) {
        this.values = new SafeList<>(values);
    }

    public static ValueArray of(List<Class<?>> values) {
        return new ClassArrayValue(values);
    }

    public ValueArray setValues(Consumer<List<Class<?>>> mutation) {
        values.modify(mutation);
        return this;
    }

    public List<Class<?>> getValues() {
        return values.getValues();
    }

    public <T> T accept(ValueArrayVisitor<T> visitor) {
        return visitor.visit(this);
    }
}