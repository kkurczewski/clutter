package io.clutter.writer.model.annotation;

import io.clutter.writer.model.annotation.param.AnnotationParams;

import java.lang.annotation.Annotation;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

final public class AnnotationType {

    private final String type;
    private final AnnotationParams values;

    public AnnotationType(String type, AnnotationParams values) {
        this.type = type;
        this.values = values;
    }

    public AnnotationType(Class<? extends Annotation> type) {
        this(type.getCanonicalName(), AnnotationParams.empty());
    }

    public AnnotationType(Class<? extends Annotation> type, AnnotationParams values) {
        this(type.getCanonicalName(), values);
    }

    public String getType() {
        return type;
    }

    public AnnotationParams getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationType that = (AnnotationType) o;
        return type.equals(that.type) && values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, values);
    }

    @Override
    public String toString() {
        return "@" + type + (getValues().isEmpty() ? "" : getValues()
                .entrySet()
                .stream()
                .map(param -> format("%s = %s", param.getKey(), param.getValue()))
                .collect(joining(", ", "(", ")")));
    }
}
