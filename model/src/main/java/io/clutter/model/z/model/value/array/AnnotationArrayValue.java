package io.clutter.model.z.model.value.array;

import io.clutter.model.z.collection.SafeList;
import io.clutter.model.z.model.annotation.Annotation;

import java.util.List;
import java.util.function.Consumer;

final public class AnnotationArrayValue implements ValueArray {

    private final SafeList<Annotation> values;

    private AnnotationArrayValue(List<Annotation> values) {
        this.values = new SafeList<>(values);
    }

    public static ValueArray of(List<Annotation> value) {
        return new AnnotationArrayValue(value);
    }

    public ValueArray setValues(Consumer<List<Annotation>> mutation) {
        values.modify(mutation);
        return this;
    }

    public List<Annotation> getValues() {
        return values.getValues();
    }

    public <T> T accept(ValueArrayVisitor<T> visitor) {
        return visitor.visit(this);
    }
}