package io.clutter.model.field;

import io.clutter.model.annotation.AnnotationT;
import io.clutter.model.common.Expression;
import io.clutter.model.common.Trait;
import io.clutter.model.common.Visibility;
import io.clutter.model.type.Type;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final public class Field {

    private final List<AnnotationT> annotations = new LinkedList<>();
    private final List<Trait> traits = new LinkedList<>();

    private Visibility visibility;
    private String name;
    private Type type;
    private Expression expression;

    private Field(
        List<AnnotationT> annotations,
        Visibility visibility,
        List<Trait> traits,
        String name,
        Type type,
        Expression expression
    ) {
        this.annotations.addAll(annotations);
        this.visibility = visibility;
        this.traits.addAll(traits);
        this.name = name;
        this.type = type;
        this.expression = expression;
    }

    public Field() {
    }

    public static Field copyOf(Field field) {
        return new Field(
            field.annotations,
            field.visibility,
            field.traits,
            field.name,
            field.type,
            field.expression
        );
    }

    public Field setAnnotations(Consumer<List<AnnotationT>> mutation) {
        mutation.accept(annotations);
        return this;
    }

    public Field setVisibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Field setTraits(Consumer<List<Trait>> mutation) {
        mutation.accept(traits);
        return this;
    }

    public Field setName(String name) {
        this.name = name;
        return this;
    }

    public Field setType(Type type) {
        this.type = type;
        return this;
    }

    public Field setExpression(Expression expression) {
        this.expression = expression;
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

    public Type getType() {
        return type;
    }

    public Expression getExpression() {
        return expression;
    }

    public <T> T accept(FieldVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return annotations.equals(field.annotations) &&
            traits.equals(field.traits) &&
            visibility == field.visibility &&
            name.equals(field.name) &&
            type.equals(field.type) &&
            Objects.equals(expression, field.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "Field{" +
            "annotations=" + annotations +
            ", traits=" + traits +
            ", visibility=" + visibility +
            ", name='" + name + '\'' +
            ", type=" + type +
            ", expression=" + expression +
            '}';
    }
}