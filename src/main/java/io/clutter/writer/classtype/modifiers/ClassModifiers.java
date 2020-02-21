package io.clutter.writer.classtype.modifiers;

import javax.lang.model.element.Modifier;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

final public class ClassModifiers {

    public static ClassModifiers PUBLIC = new ClassModifiers(ClassVisibility.PUBLIC);

    private final ClassVisibility visibility;
    private final Set<ClassTrait> trait;

    public ClassModifiers(ClassVisibility visibility, ClassTrait... trait) {
        this.visibility = visibility;
        this.trait = Set.of(trait);
    }

    public static ClassModifiers from(Set<Modifier> modifiers) {
        final ClassVisibility visibility;
        if (modifiers.contains(Modifier.PUBLIC)) {
            visibility = ClassVisibility.PUBLIC;
        } else if (modifiers.contains(Modifier.PROTECTED)) {
            visibility = ClassVisibility.PROTECTED;
        } else if (modifiers.contains(Modifier.PRIVATE)) {
            visibility = ClassVisibility.PRIVATE;
        } else {
            visibility = ClassVisibility.PACKAGE_PRIVATE;
        }
        Set<String> traitsValues = Stream.of(ClassTrait.values())
                .map(String::valueOf)
                .collect(toSet());

        ClassTrait[] traits = modifiers
                .stream()
                .map(String::valueOf)
                .map(String::toLowerCase)
                .filter(traitsValues::contains)
                .map(ClassTrait::valueOf)
                .toArray(ClassTrait[]::new);

        return new ClassModifiers(visibility, traits);
    }

    @Override
    public String toString() {
        return visibility.toString() + (trait.isEmpty() ? "" : trait.stream()
                .map(ClassTrait::toString).collect(joining(" ")));
    }
}

