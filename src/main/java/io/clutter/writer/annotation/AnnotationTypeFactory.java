package io.clutter.writer.annotation;

import io.clutter.writer.annotation.param.AnnotationParams;

import javax.lang.model.element.AnnotationMirror;

import static io.clutter.writer.annotation.param.AnnotationParam.ofAnnotationValue;

final public class AnnotationTypeFactory {

    public static AnnotationType from(AnnotationMirror annotation) {
        AnnotationParams params = new AnnotationParams();
        annotation.getElementValues()
                .forEach((key, value) -> params.add(key.getSimpleName().toString(), ofAnnotationValue(value)));
        return new AnnotationType(annotation.getAnnotationType().toString(), params);
    }
}
