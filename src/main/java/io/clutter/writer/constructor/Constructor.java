package io.clutter.writer.constructor;

import io.clutter.writer.annotation.AnnotationType;
import io.clutter.writer.constructor.modifiers.ConstructorVisibility;
import io.clutter.writer.param.Params;

import java.util.*;

import static io.clutter.writer.constructor.modifiers.ConstructorVisibility.PUBLIC;

final public class Constructor {

    private final Params params;
    private final List<AnnotationType> annotations = new LinkedList<>();
    private final List<String> body = new LinkedList<>();

    private ConstructorVisibility visibility;

    public Constructor() {
        this.params = new Params();
    }

    public Constructor(Params params) {
        this.params = params;
        this.visibility = PUBLIC;
    }

    public Constructor setVisibility(ConstructorVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Constructor setAnnotations(AnnotationType... annotations) {
        this.annotations.clear();
        Collections.addAll(this.annotations, annotations);
        return this;
    }

    public Constructor setBody(String... body) {
        this.body.clear();
        Collections.addAll(this.body, body);
        return this;
    }

    public ConstructorVisibility getVisibility() {
        return visibility;
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
        Constructor that = (Constructor) o;
        return params.equals(that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(params);
    }
}
