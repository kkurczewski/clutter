package io.clutter.javax.factory;

import io.clutter.javax.factory.annotation.AnnotationFactory;
import io.clutter.javax.factory.types.TypeFactory;
import io.clutter.javax.factory.types.WildcardTypeFactory;
import io.clutter.model.method.Method;
import io.clutter.model.method.modifiers.MethodTrait;
import io.clutter.model.method.modifiers.MethodVisibility;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.Objects;
import java.util.Set;

import static io.clutter.javax.extractor.Filters.METHOD;
import static io.clutter.model.method.modifiers.MethodVisibility.*;
import static java.lang.String.valueOf;

final public class MethodFactory {

    public static Method from(ExecutableElement method) {
        if (!METHOD.test(method)) {
            throw new IllegalArgumentException(method.getKind().name());
        }

        Set<Modifier> modifiers = method.getModifiers();
        return new Method(valueOf(method.getSimpleName()),
                TypeFactory.from(method.getReturnType()),
                ParamFactory.from(method))
                .setVisibility(visibility(modifiers))
                .setGenericParameters(WildcardTypeFactory.from(method))
                .setTraits(traits(modifiers))
                .setAnnotations(AnnotationFactory.from(method));
    }

    private static MethodTrait[] traits(Set<Modifier> javaxModifiers) {
        return javaxModifiers.stream().map(modifier -> {
            switch (modifier) {
                case ABSTRACT:
                    return MethodTrait.ABSTRACT;
                case FINAL:
                    return MethodTrait.FINAL;
                case STATIC:
                    return MethodTrait.STATIC;
                default:
                    return null;
            }
        }).filter(Objects::nonNull).toArray(MethodTrait[]::new);
    }

    private static MethodVisibility visibility(Set<Modifier> javaxModifiers) {
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
