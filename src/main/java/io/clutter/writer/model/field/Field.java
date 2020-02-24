package io.clutter.writer.model.field;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.field.modifiers.FieldTrait;
import io.clutter.writer.model.field.modifiers.FieldVisibility;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.method.modifiers.MethodTrait;

import java.util.*;

final public class Field {

    private final String name;
    private final String type;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<FieldTrait> traits = new LinkedHashSet<>();
    private FieldVisibility visibility;

    /**
     * Creates field with default private visibility
     */
    public Field(String name, String type) {
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

    public String getName() {
        return name;
    }

    public String getType() {
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
