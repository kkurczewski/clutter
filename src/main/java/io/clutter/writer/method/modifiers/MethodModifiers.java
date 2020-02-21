package io.clutter.writer.method.modifiers;

import javax.lang.model.element.Modifier;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

final public class MethodModifiers {

    public static MethodModifiers PUBLIC = new MethodModifiers(MethodVisibility.PUBLIC);

    private final MethodVisibility visibility;
    private final Set<MethodTrait> trait;

    public MethodModifiers(MethodVisibility visibility, MethodTrait... trait) {
        this.visibility = visibility;
        this.trait = Set.of(trait);
    }

    public static MethodModifiers from(Set<Modifier> modifiers) {
        final MethodVisibility visibility;
        if (modifiers.contains(Modifier.PUBLIC)) {
            visibility = MethodVisibility.PUBLIC;
        } else if (modifiers.contains(Modifier.PROTECTED)) {
            visibility = MethodVisibility.PROTECTED;
        } else if (modifiers.contains(Modifier.PRIVATE)) {
            visibility = MethodVisibility.PRIVATE;
        } else {
            visibility = MethodVisibility.PACKAGE_PRIVATE;
        }
        Set<String> traitsValues = Stream.of(MethodTrait.values())
                .map(String::valueOf)
                .collect(toSet());

        MethodTrait[] traits = modifiers
                .stream()
                .map(String::valueOf)
                .map(String::toLowerCase)
                .filter(traitsValues::contains)
                .map(MethodTrait::valueOf)
                .toArray(MethodTrait[]::new);

        return new MethodModifiers(visibility, traits);
    }

    @Override
    public String toString() {
        return visibility.toString() + (trait.isEmpty() ? "" : trait.stream()
                .map(MethodTrait::toString).collect(joining(" ")));
    }
}

