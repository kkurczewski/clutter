package io.clutter.writer.model.field;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.field.modifiers.FieldTrait;
import io.clutter.writer.model.field.modifiers.FieldVisibility;
import io.clutter.writer.model.type.Type;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

import static io.clutter.writer.model.field.modifiers.FieldTrait.FINAL;
import static io.clutter.writer.model.field.modifiers.FieldTrait.STATIC;
import static io.clutter.writer.model.field.modifiers.FieldVisibility.PUBLIC;
import static java.lang.String.valueOf;

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
        this(name, Type.from(type));
    }

    public static Field constant(String key, Object value) {
        return new Field(key, Type.from(value.getClass()))
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
        return setAnnotations(Stream.of(annotations).map(AnnotationType::of).toArray(AnnotationType[]::new));
    }

    /**
     * Set field value to raw value of passed object using {@link Object#toString()}, if actual object is {@link String} also quoting is added
     */
    public Field setValue(Object value) {
        this.value = value instanceof String ? "\"" + value + "\"" : valueOf(value);
        return this;
    }

    /**
     * Set field value to exact expression without
     */
    public Field setRawValue(String rawExpression) {
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

    public Optional<AnnotationType> getAnnotation(Class<? extends Annotation> annotation) throws NoSuchElementException {
        return annotations.stream()
                .filter(annotationType -> annotationType.isInstanceOf(annotation))
                .findFirst();
    }

    @SafeVarargs
    final public boolean isAnnotated(Class<? extends Annotation> annotation, Class<? extends Annotation>... more) {
        return getAnnotations().stream().anyMatch(a -> a.isInstanceOf(annotation, more));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return name.equals(field.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + '{' + value + '}';
    }
}
