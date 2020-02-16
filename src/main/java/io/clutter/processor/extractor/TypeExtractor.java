package io.clutter.processor.extractor;

import javax.lang.model.element.*;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

final public class TypeExtractor {

    private final String typeQualifiedName;
    private final TypeElement rootElement;

    public TypeExtractor(TypeElement rootElement) {
        this.typeQualifiedName = rootElement.getQualifiedName().toString();
        this.rootElement = rootElement;
    }

    public List<VariableElement> extractFields() {
        return extractElements()
                .stream()
                .filter(element -> element.getKind() == ElementKind.FIELD)
                .map(VariableElement.class::cast)
                .collect(Collectors.toList());
    }

    public List<VariableElement> extractAnnotatedFields(Class<? extends Annotation> annotation) {
        return extractFields()
                .stream()
                .filter(element -> element.getAnnotation(annotation) != null)
                .collect(Collectors.toList());
    }

    public List<ExecutableElement> extractMethods() {
        return extractElements()
                .stream()
                .filter(element -> element.getKind() == ElementKind.METHOD)
                .map(ExecutableElement.class::cast)
                .collect(Collectors.toList());
    }

    public List<ExecutableElement> extractAnnotatedMethods(Class<? extends Annotation> annotation) {
        return extractMethods()
                .stream()
                .filter(element -> element.getAnnotation(annotation) != null)
                .collect(Collectors.toList());
    }

    public List<? extends Element> extractElements() {
        return rootElement.getEnclosedElements();
    }

    public List<Element> extractAnnotatedElements(Class<? extends Annotation> annotation) {
        return extractElements()
                .stream()
                .filter(element -> element.getAnnotation(annotation) != null)
                .collect(Collectors.toList());
    }

    public TypeElement extractRootElement() {
        return rootElement;
    }

    public boolean isClassAnnotated(Class<? extends Annotation> annotation) {
        return rootElement.getAnnotation(annotation) != null;
    }

    public String getTypeQualifiedName() {
        return typeQualifiedName;
    }
}
