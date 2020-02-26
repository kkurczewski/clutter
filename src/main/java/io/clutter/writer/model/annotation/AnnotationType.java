package io.clutter.writer.model.annotation;

import io.clutter.writer.model.annotation.param.AnnotationParam;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

final public class AnnotationType {

    private final String type;
    private final LinkedHashSet<AnnotationParam> values = new LinkedHashSet<>();

    public AnnotationType(String type, AnnotationParam... values) {
        this.type = type;
        Collections.addAll(this.values, values);
    }

    public AnnotationType(Class<? extends Annotation> type, AnnotationParam... values) {
        this(type.getCanonicalName(), values);
    }

    public String getType() {
        return type;
    }

    public Set<AnnotationParam> getParams() {
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
}
