package io.clutter.writer.method;

import io.clutter.writer.annotation.AnnotationType;
import io.clutter.writer.method.modifiers.MethodModifiers;
import io.clutter.writer.param.Params;

import java.util.*;

final public class Method {

    private final String name;
    private final Params params;
    private final List<AnnotationType> annotations = new LinkedList<>();
    private final List<String> body = new LinkedList<>();

    private MethodModifiers modifiers;
    private String returnType;

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
}
