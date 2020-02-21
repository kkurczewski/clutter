package io.clutter.writer.field.modifiers;

import javax.lang.model.element.Modifier;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

final public class FieldModifiers {

    public static FieldModifiers PUBLIC = new FieldModifiers(FieldVisibility.PUBLIC);

    private final FieldVisibility visibility;
    private final Set<FieldTrait> trait;

    public FieldModifiers(FieldVisibility visibility, FieldTrait... trait) {
        this.visibility = visibility;
        this.trait = Set.of(trait);
    }

    public static FieldModifiers from(Set<Modifier> modifiers) {
        final FieldVisibility visibility;
        if (modifiers.contains(Modifier.PUBLIC)) {
            visibility = FieldVisibility.PUBLIC;
        } else if (modifiers.contains(Modifier.PROTECTED)) {
            visibility = FieldVisibility.PROTECTED;
        } else if (modifiers.contains(Modifier.PRIVATE)) {
            visibility = FieldVisibility.PRIVATE;
        } else {
            visibility = FieldVisibility.PACKAGE_PRIVATE;
        }
        Set<String> traitsValues = Stream.of(FieldTrait.values())
                .map(String::valueOf)
                .collect(toSet());

        FieldTrait[] traits = modifiers
                .stream()
                .map(String::valueOf)
                .map(String::toLowerCase)
                .filter(traitsValues::contains)
                .map(FieldTrait::valueOf)
                .toArray(FieldTrait[]::new);

        return new FieldModifiers(visibility, traits);
    }

    @Override
    public String toString() {
        return visibility.toString() + (trait.isEmpty() ? "" : trait.stream()
                .map(FieldTrait::toString).collect(joining(" ")));
    }
}

