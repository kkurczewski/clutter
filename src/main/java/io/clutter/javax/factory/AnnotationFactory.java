package io.clutter.javax.factory;

import io.clutter.javax.factory.visitors.AnnotationValueVisitor;
import io.clutter.model.annotation.AnnotationType;

import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;

final public class AnnotationFactory {

    private static final AnnotationValueVisitor ANNOTATION_VALUE_VISITOR = new AnnotationValueVisitor();

    @SuppressWarnings("unchecked")
    public static AnnotationType from(AnnotationMirror annotation) {
        var clazz = BoxedTypeFactory.from(annotation.getAnnotationType()).getType();
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        annotation.getElementValues().forEach((key, value) -> params.put(
                key.getSimpleName().toString(),
                value.accept(ANNOTATION_VALUE_VISITOR, TypeFactory.from(key.getReturnType()).getType())
        ));
        return new AnnotationType((Class<? extends Annotation>) clazz, params);
    }
}
