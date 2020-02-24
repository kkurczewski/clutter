package io.clutter.writer.model.annotation.param;

import java.util.Objects;

import static java.lang.String.valueOf;

final public class AnnotationAttribute {

    private final String value;

    private AnnotationAttribute(String value) {
        this.value = value;
    }

    public static AnnotationAttribute ofClass(Class<?> type) {
        return new AnnotationAttribute(type.getCanonicalName() + ".class");
    }

    public static AnnotationAttribute ofRawValue(Object type) {
        return new AnnotationAttribute(valueOf(type));
    }

    public static AnnotationAttribute ofString(String type) {
        return new AnnotationAttribute("\"" + type + "\"");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationAttribute that = (AnnotationAttribute) o;
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
