package io.clutter.model.ctor;

import io.clutter.model.annotation.AnnotationT;
import io.clutter.model.common.Expression;
import io.clutter.model.common.Visibility;
import io.clutter.model.type.Argument;
import io.clutter.model.type.GenericType;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final public class Constructor {
    private final List<AnnotationT> annotations = new LinkedList<>();
    private final List<Argument> arguments = new LinkedList<>();
    private final List<Expression> body = new LinkedList<>();
    private final List<GenericType> genericTypes = new LinkedList<>();

    private Visibility visibility;

    private Constructor(
        List<AnnotationT> annotations,
        Visibility visibility,
        List<Argument> arguments,
        List<Expression> body,
        List<GenericType> genericTypes
    ) {
        this.annotations.addAll(annotations);
        this.visibility = visibility;
        this.arguments.addAll(arguments);
        this.body.addAll(body);
        this.genericTypes.addAll(genericTypes);
    }

    public Constructor() {
    }

    public static Constructor copyOf(Constructor constructor) {
        return new Constructor(
            constructor.annotations,
            constructor.visibility,
            constructor.arguments,
            constructor.body,
            constructor.genericTypes
        );
    }

    public Constructor setAnnotations(Consumer<List<AnnotationT>> mutation) {
        mutation.accept(annotations);
        return this;
    }

    public Constructor setVisibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Constructor setArguments(Consumer<List<Argument>> mutation) {
        mutation.accept(arguments);
        return this;
    }

    public Constructor setBody(Consumer<List<Expression>> mutation) {
        mutation.accept(body);
        return this;
    }

    public Constructor setGenericTypes(Consumer<List<GenericType>> mutation) {
        mutation.accept(genericTypes);
        return this;
    }

    public List<AnnotationT> getAnnotations() {
        return List.copyOf(annotations);
    }

    public Visibility getVisibility() {
        return visibility;
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

    public <T> T accept(ConstructorVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constructor that = (Constructor) o;
        return annotations.equals(that.annotations) &&
            arguments.equals(that.arguments) &&
            body.equals(that.body) &&
            genericTypes.equals(that.genericTypes) &&
            visibility == that.visibility;
    }

    @Override
    public int hashCode() {
        return Objects.hash(arguments);
    }

    @Override
    public String toString() {
        return "Constructor{" +
            "annotations=" + annotations +
            ", arguments=" + arguments +
            ", body=" + body +
            ", genericTypes=" + genericTypes +
            ", visibility=" + visibility +
            '}';
    }
}