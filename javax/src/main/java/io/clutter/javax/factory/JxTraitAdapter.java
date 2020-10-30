package io.clutter.javax.factory;

import io.clutter.model.common.Trait;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;

final public class JxTraitAdapter {

    private JxTraitAdapter() {
    }

    public static List<Trait> from(Set<Modifier> modifiers) {
        return modifiers
            .stream()
            .map(JxTraitAdapter::from)
            .filter(Objects::nonNull)
            .collect(toList());
    }

    public static Trait from(Modifier modifier) {
        switch (modifier) {
            case ABSTRACT:
                return Trait.ABSTRACT;
            case FINAL:
                return Trait.FINAL;
            case STATIC:
                return Trait.STATIC;
        }
        return null;
    }
}
