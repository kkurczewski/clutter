package io.clutter.writer.model.method;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.method.modifiers.MethodTrait;
import io.clutter.writer.model.method.modifiers.MethodVisibility;
import io.clutter.writer.model.param.Param;

import java.util.*;

final public class Method {

    private final String name;
    private final LinkedHashSet<Param> params = new LinkedHashSet<>();
    private final String returnType;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final List<String> body = new LinkedList<>();
    private final LinkedHashSet<MethodTrait> traits = new LinkedHashSet<>();
    private MethodVisibility visibility;

    /**
     * Creates method with default public visibility
     */
    public Method(String name, String returnType, Param... params) {
        this.name = name;
        this.returnType = returnType;
        this.visibility = MethodVisibility.PUBLIC;
        Collections.addAll(this.params, params);
    }

    /**
     * Creates method with default void return type and public visibility
     */
    public Method(String name, Param... params) {
        this(name, "void", params);
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

    public Method setAnnotations(AnnotationType... annotations) {
        this.annotations.clear();
        Collections.addAll(this.annotations, annotations);
        return this;
    }

    public Set<MethodTrait> getTraits() {
        return traits;
    }

    public String getName() {
        return name;
    }

    public MethodVisibility getVisibility() {
        return visibility;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<AnnotationType> getAnnotations() {
        return annotations;
    }

    public Set<Param> getParams() {
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
        return name.equals(method.name) &&
                params.equals(method.params) &&
                returnType.equals(method.returnType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, params, returnType);
    }

    @Override
    public String toString() {
        return "Method{" +
                "name='" + name + '\'' +
                ", params=" + params +
                ", returnType='" + returnType + '\'' +
                '}';
    }
}
