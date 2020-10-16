package io.clutter.model.z.model.method;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.param.Argument;
import io.clutter.model.type.GenericType;
import io.clutter.model.type.Type;
import io.clutter.model.z.collection.SafeList;
import io.clutter.model.z.model.common.Expression;
import io.clutter.model.z.model.common.Trait;
import io.clutter.model.z.model.common.Visibility;

import java.util.List;
import java.util.function.Consumer;

public class Method extends MethodFactory {
    private final SafeList<AnnotationType> annotations;
    private Visibility visibility;
    private final SafeList<Trait> traits;
    private String name;
    private Type returnType;
    private final SafeList<Argument> arguments;
    private final SafeList<Expression> body;
    private final SafeList<GenericType> genericTypes;

    public Method(
            SafeList<AnnotationType> annotations,
            Visibility visibility,
            SafeList<Trait> traits,
            String name,
            Type returnType,
            SafeList<Argument> arguments,
            SafeList<Expression> body,
            SafeList<GenericType> genericTypes
    ) {
        this.annotations = annotations;
        this.visibility = visibility;
        this.traits = traits;
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
        this.body = body;
        this.genericTypes = genericTypes;
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

    public Method setAnnotations(Consumer<List<AnnotationType>> mutation) {
        annotations.modify(mutation);
        return this;
    }

    public Method setVisibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Method setTraits(Consumer<List<Trait>> mutation) {
        traits.modify(mutation);
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
        arguments.modify(mutation);
        return this;
    }

    public Method setBody(Consumer<List<Expression>> mutation) {
        body.modify(mutation);
        return this;
    }

    public Method setGenericTypes(Consumer<List<GenericType>> mutation) {
        genericTypes.modify(mutation);
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

    public Type getReturnType() {
        return returnType;
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

    public <T> T accept(MethodVisitor<T> visitor) {
        return visitor.visit(this);
    }
}