package io.clutter.writer.annotation.param;

import io.clutter.writer.annotation.AnnotationType;

import javax.lang.model.element.AnnotationValue;

import static java.lang.String.*;

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

    public static AnnotationParam ofAnnotationValue(AnnotationValue type) {
        return new AnnotationParam(type.toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
