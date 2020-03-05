package io.clutter.writer.model.method;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.method.modifiers.MethodTrait;
import io.clutter.writer.model.method.modifiers.MethodVisibility;
import io.clutter.writer.model.param.Param;
import io.clutter.writer.model.type.Type;
import io.clutter.writer.model.type.WildcardType;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.String.*;

final public class Method {

    private final String name;
    private final LinkedHashSet<Param> params = new LinkedHashSet<>();
    private final Type returnType;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<WildcardType> genericTypes = new LinkedHashSet<>();
    private final LinkedHashSet<MethodTrait> traits = new LinkedHashSet<>();
    private final List<String> body = new LinkedList<>();
    private MethodVisibility visibility;

    /**
     * Creates method with default public visibility
     */
    public Method(String name, Type returnType, Param... params) {
        this.name = name;
        this.returnType = returnType;
        this.visibility = MethodVisibility.PUBLIC;
        Collections.addAll(this.params, params);
    }

    /**
     * Creates method with default void return type and public visibility
     */
    public Method(String name, Param... params) {
        this(name, Type.VOID, params);
    }

    public Method setVisibility(MethodVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Method setTraits(MethodTrait... traits) {
        this.traits.clear();
        Collections.addAll(this.traits, traits);
        return this;
    }

    public Method setBody(String... body) {
        this.body.clear();
        Collections.addAll(this.body, body);
        return this;
    }

    public Method setBody(List<String> body) {
        this.body.clear();
        this.body.addAll(body);
        return this;
    }

    public Method setAnnotations(AnnotationType... annotations) {
        this.annotations.clear();
        Collections.addAll(this.annotations, annotations);
        return this;
    }

    @SafeVarargs
    final public Method setAnnotations(Class<? extends Annotation>... annotations) {
        return setAnnotations(Stream.of(annotations).map(AnnotationType::of).toArray(AnnotationType[]::new));
    }

    public Method setGenericTypes(WildcardType genericTypes) {
        this.genericTypes.clear();
        Collections.addAll(this.genericTypes, genericTypes);
        return this;
    }

    public String getName() {
        return name;
    }

    public MethodVisibility getVisibility() {
        return visibility;
    }

    public Set<MethodTrait> getTraits() {
        return traits;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Set<Param> getParams() {
        return params;
    }

    public List<String> getBody() {
        return body;
    }

    public Set<WildcardType> getGenericTypes() {
        return genericTypes;
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
        Method method = (Method) o;
        return name.equals(method.name) && params.equals(method.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, params);
    }

    @Override
    public String toString() {
        return name + params;
    }
}
