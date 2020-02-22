package io.clutter.writer.model.annotation.param;

import io.clutter.writer.model.annotation.AnnotationType;

import static java.lang.String.valueOf;

final public class AnnotationParam {

    private final String value;

    private AnnotationParam(String value) {
        this.value = value;
    }

    public static AnnotationParam ofClass(Class<?> type) {
        return new AnnotationParam(type.getCanonicalName());
    }

    public static AnnotationParam ofRawValue(Object type) {
        return new AnnotationParam(valueOf(type));
    }

    public static AnnotationParam ofString(Object type) {
        return new AnnotationParam("\"" + type + "\"");
    }

    public static AnnotationParam ofAnnotation(AnnotationType type) {
        return new AnnotationParam(valueOf(type));
    }

    @Override
    public String toString() {
        return value;
    }
}
