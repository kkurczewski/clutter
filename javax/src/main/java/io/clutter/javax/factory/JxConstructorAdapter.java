package io.clutter.javax.factory;

import io.clutter.model.common.Visibility;
import io.clutter.model.ctor.Constructor;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.Objects;
import java.util.Set;

import static io.clutter.model.common.Visibility.*;

final public class JxConstructorAdapter {

    public static Constructor from(ExecutableElement constructor) {
        if (constructor.getKind() != ElementKind.CONSTRUCTOR) {
            throw new IllegalArgumentException(constructor.getKind().name());
        }
        return new Constructor()
            .setArguments(arguments -> arguments.addAll(JxArgumentAdapter.from(constructor.getParameters())))
            .setAnnotations(annotations -> annotations.addAll(JxAnnotationAdapter.from(constructor)))
            .setGenericTypes(generics -> generics.addAll(JxGenericTypeAdapter.from(constructor
                .getTypeParameters())))
            .setVisibility(visibility(constructor.getModifiers()));
    }

    private static Visibility visibility(Set<Modifier> javaxModifiers) {
        return javaxModifiers.stream().map(modifier -> {
            switch (modifier) {
                case PUBLIC:
                    return PUBLIC;
                case PROTECTED:
                    return PROTECTED;
                case PRIVATE:
                    return PRIVATE;
                default:
                    return null;
            }
        }).filter(Objects::nonNull).findFirst().orElse(null);
    }
}
