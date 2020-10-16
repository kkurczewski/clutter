package io.clutter.model.z.model.field;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.type.Type;
import io.clutter.model.z.collection.SafeList;
import io.clutter.model.z.model.common.Expression;
import io.clutter.model.z.model.common.Trait;
import io.clutter.model.z.model.common.Visibility;

import java.util.List;
import java.util.function.Consumer;

public class Field extends FieldFactory {
    private final SafeList<AnnotationType> annotations;
    private Visibility visibility;
    private final SafeList<Trait> traits;
    private String name;
    private Type type;
    private Expression expression;

    public Field(
            SafeList<AnnotationType> annotations,
            Visibility visibility, SafeList<Trait> traits,
            String name,
            Type type,
            Expression expression
    ) {
        this.annotations = annotations;
        this.visibility = visibility;
        this.traits = traits;
        this.name = name;
        this.type = type;
        this.expression = expression;
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

    public Field setAnnotations(Consumer<List<AnnotationType>> mutation) {
        annotations.modify(mutation);
        return this;
    }

    public Field setVisibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Field setTraits(Consumer<List<Trait>> mutation) {
        traits.modify(mutation);
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

    public List<AnnotationType> getAnnotations() {
        return annotations.getValues();
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public List<Trait> getTraits() {
        return traits.getValues();
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
}