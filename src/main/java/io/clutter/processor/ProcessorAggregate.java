package io.clutter.processor;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * Aggregates map with annotated {@link TypeElement}
 */
public class ProcessorAggregate {

    private final Map<String, Set<TypeElement>> annotatedElements;

    public ProcessorAggregate(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotatedElements = annotations
                .stream()
                .collect(toMap(
                        annotation -> valueOf(annotation.getQualifiedName()),
                        annotation -> roundEnv
                                .getElementsAnnotatedWithAny(annotation)
                                .stream()
                                .filter(TypeElement.class::isInstance)
                                .map(TypeElement.class::cast)
                                .collect(toSet())));
    }

    public Set<TypeElement> get(Class<? extends Annotation> annotation) {
        return annotatedElements.get(annotation.getCanonicalName());
    }
}