package io.clutter.model.method;

import io.clutter.model.annotation.AnnotationT;
import io.clutter.model.common.Expression;
import io.clutter.model.common.Trait;
import io.clutter.model.common.Visibility;
import io.clutter.model.type.Argument;
import io.clutter.model.type.GenericType;
import io.clutter.model.type.Type;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final public class Method {
    private final List<AnnotationT> annotations = new LinkedList<>();
    private final List<Trait> traits = new LinkedList<>();
    private final List<Argument> arguments = new LinkedList<>();
    private final List<Expression> body = new LinkedList<>();
    private final List<GenericType> genericTypes = new LinkedList<>();

    private Visibility visibility;

    private String name;
    private Type returnType;

    private Method(
        List<AnnotationT> annotations,
        Visibility visibility,
        List<Trait> traits,
        String name,
        Type returnType,
        List<Argument> arguments,
        List<Expression> body,
        List<GenericType> genericTypes
    ) {
        this.annotations.addAll(annotations);
        this.visibility = visibility;
        this.traits.addAll(traits);
        this.name = name;
        this.returnType = returnType;
        this.arguments.addAll(arguments);
        this.body.addAll(body);
        this.genericTypes.addAll(genericTypes);
    }

    public Method() {
    }

    public static Method copyOf(Method method) {
        return new Method(
            method.annotations,
            method.visibility,
            method.traits,
            method.name,
            method.returnType,
            method.arguments,
            method.body,
            method.genericTypes
        );
    }

    public Method setAnnotations(Consumer<List<AnnotationT>> mutation) {
        mutation.accept(annotations);
        return this;
    }

    public Method setVisibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Method setTraits(Consumer<List<Trait>> mutation) {
        mutation.accept(traits);
        return this;
    }

    public Method setName(String name) {
        this.name = name;
        return this;
    }

    public Method setReturnType(Type returnType) {
        this.returnType = returnType;
        return this;
    }

    public Method setArguments(Consumer<List<Argument>> mutation) {
        mutation.accept(arguments);
        return this;
    }

    public Method setBody(Consumer<List<Expression>> mutation) {
        mutation.accept(body);
        return this;
    }

    public Method setGenericTypes(Consumer<List<GenericType>> mutation) {
        mutation.accept(genericTypes);
        return this;
    }

    public List<AnnotationT> getAnnotations() {
        return List.copyOf(annotations);
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public List<Trait> getTraits() {
        return List.copyOf(traits);
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<Argument> getArguments() {
        return List.copyOf(arguments);
    }

    public List<Expression> getBody() {
        return List.copyOf(body);
    }

    public List<GenericType> getGenericTypes() {
        return List.copyOf(genericTypes);
    }

    public <T> T accept(MethodVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Method method = (Method) o;
        return annotations.equals(method.annotations) &&
            traits.equals(method.traits) &&
            arguments.equals(method.arguments) &&
            body.equals(method.body) &&
            genericTypes.equals(method.genericTypes) &&
            visibility == method.visibility &&
            name.equals(method.name) &&
            returnType.equals(method.returnType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arguments, name, returnType);
    }

    @Override
    public String toString() {
        return "Method{" +
            "annotations=" + annotations +
            ", traits=" + traits +
            ", arguments=" + arguments +
            ", body=" + body +
            ", genericTypes=" + genericTypes +
            ", visibility=" + visibility +
            ", name='" + name + '\'' +
            ", returnType=" + returnType +
            '}';
    }
}