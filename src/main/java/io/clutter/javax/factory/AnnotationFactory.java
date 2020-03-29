package io.clutter.javax.factory;

import io.clutter.javax.factory.types.BoxedTypeFactory;
import io.clutter.javax.factory.types.TypeFactory;
import io.clutter.javax.factory.visitors.AnnotationValueVisitor;
import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.annotation.param.AnnotationValue;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;

final public class AnnotationFactory {

    private static final AnnotationValueVisitor ANNOTATION_VALUE_VISITOR = new AnnotationValueVisitor();

    public static AnnotationType from(AnnotationMirror annotation) {
        var clazz = BoxedTypeFactory.from(annotation.getAnnotationType()).getType();
        LinkedHashMap<String, AnnotationValue> params = new LinkedHashMap<>();
        annotation.getElementValues().forEach((key, value) -> params.put(
                key.getSimpleName().toString(),
                value.accept(ANNOTATION_VALUE_VISITOR, TypeFactory.from(key.getReturnType()).getType())
        ));
        return new AnnotationType(castToAnnotation(clazz), params);
    }

    public static AnnotationType[] from(AnnotatedConstruct annotatedConstruct) {
        return annotatedConstruct
                .getAnnotationMirrors()
                .stream()
                .map(AnnotationFactory::from)
                .toArray(AnnotationType[]::new);
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Annotation> castToAnnotation(Class<?> clazz) {
        return (Class<? extends Annotation>) clazz;
    }
}
