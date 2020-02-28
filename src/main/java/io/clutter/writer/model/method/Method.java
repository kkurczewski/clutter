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

final public class Method {

    private final String name;
    private final LinkedHashSet<Param> params = new LinkedHashSet<>();
    private final Type returnType;
    private WildcardType genericType;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final List<String> body = new LinkedList<>();
    private final LinkedHashSet<MethodTrait> traits = new LinkedHashSet<>();
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

    public Method setAnnotations(AnnotationType... annotations) {
        this.annotations.clear();
        Collections.addAll(this.annotations, annotations);
        return this;
    }

    @SafeVarargs
    final public Method setAnnotations(Class<? extends Annotation>... annotations) {
        return setAnnotations(Stream.of(annotations).map(AnnotationType::new).toArray(AnnotationType[]::new));
    }

    public Method setGenericType(WildcardType genericType) {
        this.genericType = genericType;
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

    public List<AnnotationType> getAnnotations() {
        return annotations;
    }

    public Set<Param> getParams() {
        return params;
    }

    public List<String> getBody() {
        return body;
    }

    public Optional<WildcardType> getGenericType() {
        return Optional.ofNullable(genericType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Method method = (Method) o;
        return name.equals(method.name) &&
                params.equals(method.params) &&
                returnType.equals(method.returnType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, params, returnType);
    }

    @Override
    public String toString() {
        return "Method{" +
                "name='" + name + '\'' +
                ", params=" + params +
                ", returnType='" + returnType + '\'' +
                '}';
    }
}
