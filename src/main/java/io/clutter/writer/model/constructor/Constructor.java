package io.clutter.writer.model.constructor;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.constructor.modifiers.ConstructorVisibility;
import io.clutter.writer.model.param.Param;
import io.clutter.writer.model.type.WildcardType;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

import static io.clutter.writer.model.constructor.modifiers.ConstructorVisibility.PUBLIC;

final public class Constructor {

    private final LinkedHashSet<Param> params = new LinkedHashSet<>();

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final List<String> body = new LinkedList<>();
    private final LinkedHashSet<WildcardType> genericTypes = new LinkedHashSet<>();
    private ConstructorVisibility visibility;

    public Constructor(Param... params) {
        Collections.addAll(this.params, params);
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

    @SafeVarargs
    final public Constructor setAnnotations(Class<? extends Annotation>... annotations) {
        return setAnnotations(Stream.of(annotations).map(AnnotationType::new).toArray(AnnotationType[]::new));
    }

    public Constructor setBody(String... body) {
        this.body.clear();
        Collections.addAll(this.body, body);
        return this;
    }

    public Constructor setGenericTypes(WildcardType genericTypes) {
        this.genericTypes.clear();
        Collections.addAll(this.genericTypes, genericTypes);
        return this;
    }

    public ConstructorVisibility getVisibility() {
        return visibility;
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

    public Set<WildcardType> getGenericTypes() {
        return genericTypes;
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
