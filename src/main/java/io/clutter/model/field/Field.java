package io.clutter.model.field;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.field.modifiers.FieldTrait;
import io.clutter.model.field.modifiers.FieldVisibility;
import io.clutter.model.type.Type;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

import static io.clutter.model.field.modifiers.FieldTrait.FINAL;
import static io.clutter.model.field.modifiers.FieldTrait.STATIC;
import static io.clutter.model.field.modifiers.FieldVisibility.PUBLIC;

final public class Field {

    private final String name;
    private final Type type;

    private final List<AnnotationType> annotations = new ArrayList<>();
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

    public Field(String name, Class<?> type) {
        this(name, Type.of(type));
    }

    /**
     * Creates public static final variable with given raw value
     */
    public static Field constant(String key, String value) {
        return new Field(key, Type.of(value.getClass()))
                .setValue(value)
                .setVisibility(PUBLIC)
                .setTraits(STATIC, FINAL);
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
        return setAnnotations(Stream.of(annotations)
                .map(AnnotationType::new)
                .toArray(AnnotationType[]::new));
    }

    public Field setValue(String rawExpression) {
        this.value = rawExpression;
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

    public Set<FieldTrait> getTraits() {
        return traits;
    }

    public Optional<String> getValue() {
        return Optional.ofNullable(value);
    }

    public List<AnnotationType> getAnnotations() {
        return annotations;
    }

    public <T extends Annotation> Optional<T> getAnnotation(Class<T> annotation) {
        return annotations.stream()
                .filter(annotationType -> annotationType.isInstanceOf(annotation))
                .findFirst()
                .map(AnnotationType::reflect);
    }

    @SafeVarargs
    final public boolean isAnnotated(Class<? extends Annotation> annotation, Class<? extends Annotation>... more) {
        return getAnnotations().stream().anyMatch(it -> it.isInstanceOf(annotation, more));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return name.equals(field.name) &&
                type.equals(field.type) &&
                annotations.equals(field.annotations) &&
                traits.equals(field.traits) &&
                visibility == field.visibility &&
                Objects.equals(value, field.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", annotations=" + annotations +
                ", traits=" + traits +
                ", visibility=" + visibility +
                ", value='" + value + '\'' +
                '}';
    }
}
