package io.clutter.javax.factory;

import io.clutter.javax.factory.types.WildcardTypeFactory;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.constructor.modifiers.ConstructorVisibility;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.Objects;
import java.util.Set;

import static io.clutter.model.constructor.modifiers.ConstructorVisibility.*;

final public class ConstructorFactory {

    public static Constructor from(ExecutableElement constructor) {
        if (constructor.getKind() != ElementKind.CONSTRUCTOR) {
            throw new IllegalArgumentException(constructor.getKind().name());
        }

        return new Constructor(ParamFactory.from(constructor))
                .setAnnotations(AnnotationFactory.from(constructor))
                .setBody()
                .setGenericParameters(WildcardTypeFactory.from(constructor))
                .setVisibility(visibility(constructor.getModifiers()));
    }

    private static ConstructorVisibility visibility(Set<Modifier> javaxModifiers) {
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
        }).filter(Objects::nonNull).findFirst().orElse(PACKAGE_PRIVATE);
    }
}
