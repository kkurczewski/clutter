package io.clutter.writer.model.field.modifiers;

import java.util.Set;

import static java.util.stream.Collectors.joining;

final public class FieldModifiers {

    public static final FieldModifiers PRIVATE = new FieldModifiers(FieldVisibility.PRIVATE);

    private final FieldVisibility visibility;
    private final Set<FieldTrait> trait;

    public FieldModifiers(FieldVisibility visibility, FieldTrait... trait) {
        this.visibility = visibility;
        this.trait = Set.of(trait);
    }

    @Override
    public String toString() {
        return visibility.toString() + (trait.isEmpty() ? "" : trait.stream()
                .map(FieldTrait::toString).collect(joining(" ")));
    }
}

