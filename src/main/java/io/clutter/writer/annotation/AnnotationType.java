package io.clutter.writer.annotation;

import io.clutter.writer.annotation.param.AnnotationParam;
import io.clutter.writer.annotation.param.AnnotationParams;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.joining;

final public class AnnotationType {

    private final String type;
    private final AnnotationParams values;

    public AnnotationType(String type, AnnotationParams values) {
        this.type = type;
        this.values = values;
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
    public String toString() {
        return "@" + type + (getValues().isEmpty() ? "" : getValues()
                .entrySet()
                .stream()
                .map(param -> {
                    AnnotationParam[] params = param.getValue();
                    String stringParams = (params.length > 1) ? Arrays.toString(params) : valueOf(params[0]);
                    return format("%s = %s", param.getKey(), stringParams);
                })
                .collect(joining(", ", "(", ")")));
    }
}
