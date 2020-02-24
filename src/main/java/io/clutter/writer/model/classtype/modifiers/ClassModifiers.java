package io.clutter.writer.model.classtype.modifiers;

import java.util.Set;

import static java.util.stream.Collectors.joining;

final public class ClassModifiers {

    public static ClassModifiers PUBLIC = new ClassModifiers(ClassVisibility.PUBLIC);

    private final ClassVisibility visibility;
    private final Set<ClassTrait> trait;

    public ClassModifiers(ClassVisibility visibility, ClassTrait... trait) {
        this.visibility = visibility;
        this.trait = Set.of(trait);
    }

    @Override
    public String toString() {
        return visibility.toString() + " " + (trait.isEmpty() ? "" : trait.stream()
                .map(ClassTrait::toString).collect(joining(" ")));
    }
}

