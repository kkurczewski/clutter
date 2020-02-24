package io.clutter.javax.factory;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.annotation.param.AnnotationParams;

import javax.lang.model.element.AnnotationMirror;

import static io.clutter.writer.model.annotation.param.AnnotationAttribute.ofRawValue;

final public class AnnotationTypeFactory {

    public static AnnotationType from(AnnotationMirror annotation) {
        AnnotationParams params = new AnnotationParams();
        annotation.getElementValues()
                .forEach((key, value) -> params.add(key.getSimpleName().toString(), ofRawValue(value)));
        return new AnnotationType(annotation.getAnnotationType().toString(), params);
    }
}
