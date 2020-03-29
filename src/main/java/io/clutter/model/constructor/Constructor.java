package io.clutter.model.constructor;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.constructor.modifiers.ConstructorVisibility;
import io.clutter.model.param.Param;
import io.clutter.model.type.WildcardType;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

import static io.clutter.model.constructor.modifiers.ConstructorVisibility.PUBLIC;
import static java.lang.String.*;

final public class Constructor {

    private final LinkedHashSet<Param> params = new LinkedHashSet<>();

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final List<String> body = new LinkedList<>();
    private final LinkedHashSet<WildcardType> wildcardTypes = new LinkedHashSet<>();
    private ConstructorVisibility visibility;

    public Constructor(Param... params) {
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
        return setAnnotations(Stream.of(annotations).map(AnnotationType::new).toArray(AnnotationType[]::new));
    }

    public Constructor setBody(String... body) {
        this.body.clear();
        Collections.addAll(this.body, body);
        return this;
    }

    public Constructor setBody(List<String> body) {
        this.body.clear();
        this.body.addAll(body);
        return this;
    }

    public Constructor setWildcardTypes(WildcardType wildcardTypes) {
        this.wildcardTypes.clear();
        Collections.addAll(this.wildcardTypes, wildcardTypes);
        return this;
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

    public Set<WildcardType> getWildcardTypes() {
        return wildcardTypes;
    }

    public List<AnnotationType> getAnnotations() {
        return annotations;
    }

    public Optional<AnnotationType> getAnnotation(Class<? extends Annotation> annotation) throws NoSuchElementException {
        return annotations.stream()
                .filter(annotationType -> annotationType.isInstanceOf(annotation))
                .findFirst();
    }

    @SafeVarargs
    final public boolean isAnnotated(Class<? extends Annotation> annotation, Class<? extends Annotation>... more) {
        return getAnnotations().stream().anyMatch(a -> a.isInstanceOf(annotation, more));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constructor that = (Constructor) o;
        return params.equals(that.params);
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
                ", wildcardTypes=" + wildcardTypes +
                ", visibility=" + visibility +
                '}';
    }
}
