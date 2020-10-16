package io.clutter.model.method;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.field.Field;
import io.clutter.model.method.modifiers.MethodTrait;
import io.clutter.model.method.modifiers.MethodVisibility;
import io.clutter.model.param.Argument;
import io.clutter.model.type.Type;
import io.clutter.model.type.GenericType;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.clutter.model.type.PrimitiveType.VOID;
import static java.lang.String.format;

final public class Method {

    private final String name;
    private final LinkedHashSet<Argument> params = new LinkedHashSet<>();
    private final Type returnType;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<GenericType> genericParameters = new LinkedHashSet<>();
    private final LinkedHashSet<MethodTrait> traits = new LinkedHashSet<>();
    private final List<String> body = new LinkedList<>();

    private MethodVisibility visibility;

    /**
     * Creates method with default public visibility
     */
    public Method(String name, Type returnType, Argument... params) {
        this.name = name;
        this.returnType = returnType;
        this.visibility = MethodVisibility.PUBLIC;
        Collections.addAll(this.params, params);
    }

    /**
     * Creates method with default public visibility
     */
    public Method(String name, Class<?> returnType, Argument... params) {
        this(name, Type.of(returnType), params);
    }

    /**
     * Creates method with default void return type and public visibility
     */
    public Method(String name, Argument... params) {
        this(name, VOID, params);
    }

    /**
     * Creates getter for given field and uses given function to construct getter name
     */
    public static Method getter(Field field, Function<String, String> getterNaming) {
        return new Method(getterNaming.apply(field.getName()), field.getType())
                .setBody(format("return this.%s;", field.getName()));
    }

    /**
     * Creates setter for given field and uses given function to construct name
     */
    public static Method setter(Field field, Function<String, String> setterNaming) {
        String fieldName = field.getName();
        return new Method(setterNaming.apply(fieldName), new Argument(fieldName, field.getType()))
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
        return setAnnotations(Stream.of(annotations).map(AnnotationType::new).toArray(AnnotationType[]::new));
    }

    public Method setGenericParameters(GenericType... genericParameters) {
        this.genericParameters.clear();
        Collections.addAll(this.genericParameters, genericParameters);
        return this;
    }

    public String getName() {
        return name;
    }

    public MethodVisibility getVisibility() {
        return visibility;
    }

    public List<MethodTrait> getTraits() {
        return new ArrayList<>(traits);
    }

    public Type getReturnType() {
        return returnType;
    }

    public Set<Argument> getParams() {
        return params;
    }

    public List<String> getBody() {
        return body;
    }

    public Set<GenericType> getGenericParameters() {
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
        Method method = (Method) o;
        return name.equals(method.name) &&
                params.equals(method.params) &&
                returnType.equals(method.returnType) &&
                annotations.equals(method.annotations) &&
                genericParameters.equals(method.genericParameters) &&
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
                ", genericParameters=" + genericParameters +
                ", traits=" + traits +
                ", body=" + body +
                ", visibility=" + visibility +
                '}';
    }
}
