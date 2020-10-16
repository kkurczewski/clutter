package io.clutter.model.z.model.ctor;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.param.Argument;
import io.clutter.model.type.GenericType;
import io.clutter.model.type.Type;
import io.clutter.model.z.collection.SafeList;
import io.clutter.model.z.model.common.Expression;
import io.clutter.model.z.model.common.Visibility;

import java.util.List;
import java.util.function.Consumer;

public class Constructor extends ConstructorFactory {
    private final SafeList<AnnotationType> annotations;
    private Visibility visibility;
    private Type ownerClass;
    private final SafeList<Argument> arguments;
    private final SafeList<Expression> body;
    private final SafeList<GenericType> genericTypes;

    public Constructor(
            SafeList<AnnotationType> annotations,
            Visibility visibility,
            Type ownerClass,
            SafeList<Argument> arguments,
            SafeList<Expression> body,
            SafeList<GenericType> genericTypes
    ) {
        this.annotations = annotations;
        this.visibility = visibility;
        this.ownerClass = ownerClass;
        this.arguments = arguments;
        this.body = body;
        this.genericTypes = genericTypes;
    }

    public static Constructor copyOf(Constructor constructor) {
        return new Constructor(
                constructor.annotations,
                constructor.visibility,
                constructor.ownerClass,
                constructor.arguments,
                constructor.body,
                constructor.genericTypes
        );
    }

    public Constructor setAnnotations(Consumer<List<AnnotationType>> mutation) {
        annotations.modify(mutation);
        return this;
    }

    public Constructor setVisibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Constructor setOwnerClass(Type ownerClass) {
        this.ownerClass = ownerClass;
        return this;
    }

    public Constructor setArguments(Consumer<List<Argument>> mutation) {
        arguments.modify(mutation);
        return this;
    }

    public Constructor setBody(Consumer<List<Expression>> mutation) {
        body.modify(mutation);
        return this;
    }

    public Constructor setGenericTypes(Consumer<List<GenericType>> mutation) {
        genericTypes.modify(mutation);
        return this;
    }

    public List<AnnotationType> getAnnotations() {
        return annotations.getValues();
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public Type getOwnerClass() {
        return ownerClass;
    }

    public List<Argument> getArguments() {
        return arguments.getValues();
    }

    public List<Expression> getBody() {
        return body.getValues();
    }

    public List<GenericType> getGenericTypes() {
        return genericTypes.getValues();
    }

    public <T> T accept(ConstructorVisitor<T> visitor) {
        return visitor.visit(this);
    }
}