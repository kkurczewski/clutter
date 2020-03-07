package io.clutter.javax.factory;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.annotation.param.AnnotationParam;

import javax.lang.model.element.AnnotationMirror;

import static java.lang.String.valueOf;

final public class AnnotationTypeFactory {

    public static AnnotationType from(AnnotationMirror annotation) {
        return AnnotationType.raw(
                valueOf(annotation.getAnnotationType()),
                annotation
                        .getElementValues()
                        .entrySet()
                        .stream()
                        .map(entry -> AnnotationParam.ofRaw(
                                valueOf(entry.getKey().getSimpleName()),
                                entry.getValue().getValue())
                        ).toArray(AnnotationParam[]::new)
        );
    }
}
