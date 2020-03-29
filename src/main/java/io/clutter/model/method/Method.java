package io.clutter.model.method;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.method.modifiers.MethodTrait;
import io.clutter.model.method.modifiers.MethodVisibility;
import io.clutter.model.param.Param;
import io.clutter.model.type.Type;
import io.clutter.model.type.WildcardType;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.String.format;

final public class Method {

    private final String name;
    private final LinkedHashSet<Param> params = new LinkedHashSet<>();
    private final Type returnType;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<WildcardType> wildcardTypes = new LinkedHashSet<>();
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
     * Creates method with default public visibility
     */
    public Method(String name, Class<?> returnType, Param... params) {
        this(name, Type.of(returnType), params);
    }

    /**
     * Creates method with default void return type and public visibility
     */
    public Method(String name, Param... params) {
        this(name, Type.of(void.class), params);
    }

    public static Method getter(Type type, String fieldName, Function<String, String> getterNaming) {
        return new Method(getterNaming.apply(fieldName), type).setBody(format("return this.%s;", fieldName));
    }

    public static Method setter(Type type, String fieldName, Function<String, String> setterNaming) {
        return new Method(setterNaming.apply(fieldName), Param.of(fieldName, type))
                .setBody(format("this.%s = %s;", fieldName, fieldName));
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

    public Method setGenericParameters(WildcardType wildcardTypes) {
        this.wildcardTypes.clear();
        Collections.addAll(this.wildcardTypes, wildcardTypes);
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
        Method method = (Method) o;
        return name.equals(method.name) &&
                params.equals(method.params) &&
                returnType.equals(method.returnType) &&
                annotations.equals(method.annotations) &&
                wildcardTypes.equals(method.wildcardTypes) &&
                traits.equals(method.traits) &&
                body.equals(method.body) &&
                visibility == method.visibility;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, params);
    }

    @Override
    public String toString() {
        return "Method{" +
                "name='" + name + '\'' +
                ", params=" + params +
                ", returnType=" + returnType +
                ", annotations=" + annotations +
                ", wildcardTypes=" + wildcardTypes +
                ", traits=" + traits +
                ", body=" + body +
                ", visibility=" + visibility +
                '}';
    }
}
