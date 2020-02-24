package io.clutter.writer.model.method;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.method.modifiers.MethodModifiers;
import io.clutter.writer.model.param.Params;

import java.util.*;

final public class Method {

    private final String name;
    private final Params params;
    private final List<AnnotationType> annotations = new LinkedList<>();
    private final List<String> body = new LinkedList<>();

    private MethodModifiers modifiers;
    private String returnType;

    /**
     * Creates method with default void return type and public visibility
     */
    public Method(String name, Params params) {
        this.name = name;
        this.params = params;
        this.modifiers = MethodModifiers.PUBLIC;
        this.returnType = "void";
    }

    public Method setModifiers(MethodModifiers modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public Method setReturnType(String returnType) {
        this.returnType = returnType;
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

    public String getName() {
        return name;
    }

    public MethodModifiers getModifiers() {
        return modifiers;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<AnnotationType> getAnnotations() {
        return annotations;
    }

    public Params getParams() {
        return params;
    }

    public List<String> getBody() {
        return body;
    }

    // TODO add returnType to equals and add final modifier
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

    /**
     * Method should be used only for diagnostic
     */
    @Deprecated
    @Override
    public String toString() {
        return name + params;
    }
}
