package io.clutter.writer.model.field;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.field.modifiers.FieldTrait;
import io.clutter.writer.model.field.modifiers.FieldVisibility;
import io.clutter.writer.model.type.Type;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

final public class Field {

    private final String name;
    private final Type type;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<FieldTrait> traits = new LinkedHashSet<>();
    private FieldVisibility visibility;
    private String value;

    /**
     * Creates field with default private visibility
     */
    public Field(String name, Type type) {
        this.name = name;
        this.type = type;
        this.visibility = FieldVisibility.PRIVATE;
    }

    public Field setVisibility(FieldVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Field setTraits(FieldTrait... traits) {
        this.traits.clear();
        Collections.addAll(this.traits, traits);
        return this;
    }

    public Field setAnnotations(AnnotationType... annotations) {
        this.annotations.clear();
        Collections.addAll(this.annotations, annotations);
        return this;
    }

    @SafeVarargs
    final public Field setAnnotations(Class<? extends Annotation>... annotations) {
        return setAnnotations(Stream.of(annotations).map(AnnotationType::new).toArray(AnnotationType[]::new));
    }

    public Field setValue(String value) {
        this.value = value;
        return this;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public FieldVisibility getVisibility() {
        return visibility;
    }

    public LinkedHashSet<FieldTrait> getTraits() {
        return traits;
    }

    public List<AnnotationType> getAnnotations() {
        return annotations;
    }

    public Optional<String> getValue() {
        return Optional.ofNullable(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return name.equals(field.name) &&
                type.equals(field.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
