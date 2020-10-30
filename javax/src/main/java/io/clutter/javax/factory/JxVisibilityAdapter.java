package io.clutter.javax.factory;

import io.clutter.model.common.Visibility;

import javax.lang.model.element.Modifier;
import java.util.Objects;
import java.util.Set;

import static io.clutter.model.common.Visibility.*;

final public class JxVisibilityAdapter {

    private JxVisibilityAdapter() {
    }

    public static Visibility from(Set<Modifier> javaxModifiers) {
        return javaxModifiers
            .stream()
            .map(JxVisibilityAdapter::from)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }

    public static Visibility from(Modifier javaxModifier) {
        switch (javaxModifier) {
            case PUBLIC:
                return PUBLIC;
            case PROTECTED:
                return PROTECTED;
            case PRIVATE:
                return PRIVATE;
            default:
                return null;
        }
    }
}
