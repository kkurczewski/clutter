package io.clutter.javax.factory;

import io.clutter.model.annotation.AnnotationT;

import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

final public class JxAnnotationAdapter {

    private static final JxAnnotationValueVisitor ANNOTATION_VALUE_VISITOR = new JxAnnotationValueVisitor();

    public static AnnotationT from(AnnotationMirror annotation) {
        var type = JxBoxedTypeAdapter.from(annotation.getAnnotationType());
        var arguments = annotation.getElementValues()
            .entrySet()
            .stream()
            .collect(toMap(
                param -> param.getKey().getSimpleName().toString(),
                param -> param.getValue().accept(
                    ANNOTATION_VALUE_VISITOR,
                    JxTypeAdapter.from(param.getKey().getReturnType()).getType())
            ));
        return new AnnotationT(type).setParams(args -> args.putAll(arguments));
    }

    public static List<AnnotationT> from(AnnotatedConstruct annotatedConstruct) {
        return annotatedConstruct
            .getAnnotationMirrors()
            .stream()
            .map(JxAnnotationAdapter::from)
            .collect(toList());
    }
}
