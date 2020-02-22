package io.clutter.writer.model.method.modifiers;

import java.util.Set;

import static java.util.stream.Collectors.joining;

final public class MethodModifiers {

    public static MethodModifiers PUBLIC = new MethodModifiers(MethodVisibility.PUBLIC);

    private final MethodVisibility visibility;
    private final Set<MethodTrait> trait;

    public MethodModifiers(MethodVisibility visibility, MethodTrait... trait) {
        this.visibility = visibility;
        this.trait = Set.of(trait);
    }

    @Override
    public String toString() {
        return visibility.toString() + (trait.isEmpty() ? "" : trait.stream()
                .map(MethodTrait::toString).collect(joining(" ")));
    }
}

