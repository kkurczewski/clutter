package io.clutter.model.value;

import java.util.List;

final public class ListValue implements Value {

    private final List<Value> values;

    private ListValue(List<Value> values) {
        this.values = values;
    }

    public static ListValue of(List<Value> values) {
        return new ListValue(values);
    }

    public List<Value> getValues() {
        return List.copyOf(values);
    }

    @Override
    public <T> T accept(ValueVisitor<T> visitor) {
        return visitor.visit(this);
    }
}