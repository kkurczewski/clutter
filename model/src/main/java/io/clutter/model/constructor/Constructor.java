package io.clutter.model.constructor;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.constructor.modifiers.ConstructorVisibility;
import io.clutter.model.param.Param;
import io.clutter.model.type.WildcardType;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

import static io.clutter.model.constructor.modifiers.ConstructorVisibility.PUBLIC;

final public class Constructor {

    private final LinkedHashSet<Param> params = new LinkedHashSet<>();
    private final String className;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final List<String> body = new LinkedList<>();
    private final LinkedHashSet<WildcardType> genericParameters = new LinkedHashSet<>();
    private ConstructorVisibility visibility;

    /**
     * Creates constructor with default public visibility
     */
    public Constructor(String className, Param... params) {
        this.className = className;
        Collections.addAll(this.params, params);
        this.visibility = PUBLIC;
    }

    public Constructor setVisibility(ConstructorVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Constructor setAnnotations(AnnotationType... annotations) {
        this.annotations.clear();
        Collections.addAll(this.annotations, annotations);
        return this;
    }

    @SafeVarargs
    final public Constructor setAnnotations(Class<? extends Annotation>... annotations) {
        return setAnnotations(Stream.of(annotations)
                .map(AnnotationType::new)
                .toArray(AnnotationType[]::new));
    }

    public Constructor setBody(String... body) {
        this.body.clear();
        Collections.addAll(this.body, body);
        return this;
    }

    public Constructor setBody(Collection<String> body) {
        this.body.clear();
        this.body.addAll(body);
        return this;
    }

    public Constructor setGenericParameters(WildcardType... genericParameters) {
        this.genericParameters.clear();
        Collections.addAll(this.genericParameters, genericParameters);
        return this;
    }

    public String getClassName() {
        return className;
    }

    public ConstructorVisibility getVisibility() {
        return visibility;
    }

    public Set<Param> getParams() {
        return params;
    }

    public List<String> getBody() {
        return body;
    }

    public Set<WildcardType> getGenericParameters() {
        return genericParameters;
    }

    public List<AnnotationType> getAnnotations() {
        return annotations;
    }

    public <T extends Annotation> Optional<T> getAnnotation(Class<T> annotation) {
        return annotations.stream()
                .filter(annotationType -> annotationType.isInstanceOf(annotation))
                .findFirst()
                .map(AnnotationType::reflect);
    }

    @SafeVarargs
    final public boolean isAnnotated(Class<? extends Annotation> annotation, Class<? extends Annotation>... more) {
        return getAnnotations().stream().anyMatch(it -> it.isInstanceOf(annotation, more));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constructor that = (Constructor) o;
        return params.equals(that.params) &&
                annotations.equals(that.annotations) &&
                body.equals(that.body) &&
                genericParameters.equals(that.genericParameters) &&
                visibility == that.visibility;
    }

    @Override
    public int hashCode() {
        return Objects.hash(params);
    }

    @Override
    public String toString() {
        return "Constructor{" +
                "params=" + params +
                ", annotations=" + annotations +
                ", body=" + body +
                ", genericParameters=" + genericParameters +
                ", visibility=" + visibility +
                '}';
    }
}
