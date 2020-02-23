package io.clutter.writer.model.method.modifiers;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static io.clutter.writer.model.method.modifiers.MethodTrait.*;
import static java.util.stream.Collectors.joining;

final public class MethodModifiers {

    public static MethodModifiers PUBLIC = new MethodModifiers(MethodVisibility.PUBLIC);

    private final MethodVisibility visibility;
    private final Set<MethodTrait> traits = new LinkedHashSet<>();

    public MethodModifiers(MethodVisibility visibility, MethodTrait... traits) {
        this.visibility = visibility;
        this.traits.addAll(Arrays.asList(traits));
    }

    public MethodModifiers addTrait(MethodTrait trait) {
        traits.add(trait);
        return this;
    }

    @Override
    public String toString() {
        return visibility.toString() + " " + (traits.isEmpty() ? "" : traits.stream()
                .map(MethodTrait::toString).collect(joining(" ")));
    }

    public boolean isAbstract() {
        return traits.contains(ABSTRACT);
    }
}

