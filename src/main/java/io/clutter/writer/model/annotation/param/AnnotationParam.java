package io.clutter.writer.model.annotation.param;

import java.util.Objects;

import static java.lang.String.valueOf;

final public class AnnotationParam {

    private final String value;

    private AnnotationParam(String value) {
        this.value = value;
    }

    public static AnnotationParam ofClass(Class<?> type) {
        return new AnnotationParam(type.getCanonicalName() + ".class");
    }

    public static AnnotationParam ofRawValue(Object type) {
        return new AnnotationParam(valueOf(type));
    }

    public static AnnotationParam ofString(Object type) {
        return new AnnotationParam("\"" + type + "\"");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationParam that = (AnnotationParam) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
