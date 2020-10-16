package io.clutter.model.z.model.value;

import io.clutter.model.z.model.annotation.Annotation;

final public class AnnotationValue implements Value {

    private final Annotation value;

    private AnnotationValue(Annotation value) {
        this.value = value;
    }

    public static Value of(Annotation value) {
        return new AnnotationValue(value);
    }

    public Annotation getValue() {
        return value;
    }

    @Override
    public <T> T accept(ValueVisitor<T> visitor) {
        return visitor.visit(this);
    }
}