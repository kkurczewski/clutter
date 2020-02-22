package io.clutter.writer.model.field;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.field.modifiers.FieldModifiers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static io.clutter.writer.model.field.modifiers.FieldModifiers.PRIVATE;

final public class Field {

    private final String name;
    private final String type;
    private final List<AnnotationType> annotations = new LinkedList<>();

    private FieldModifiers modifiers;

    /**
     * Creates field with default private visibility
     */
    public Field(String name, String type) {
        this.name = name;
        this.type = type;
        this.modifiers = PRIVATE;
    }

    public Field setModifiers(FieldModifiers modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public Field setAnnotations(AnnotationType... annotations) {
        this.annotations.clear();
        Collections.addAll(this.annotations, annotations);
        return this;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public FieldModifiers getModifiers() {
        return modifiers;
    }

    public List<AnnotationType> getAnnotations() {
        return annotations;
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
