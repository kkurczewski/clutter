package io.clutter.javax.factory;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.field.Field;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.function.Function;

import static io.clutter.javax.extractor.Filters.*;
import static java.lang.String.valueOf;

final public class FieldFactory {

    public static Field from(VariableElement field) {
        return new Field(valueOf(field.getSimpleName()), TypeFactory.from(field.asType()))
                .setAnnotations(field
                        .getAnnotationMirrors()
                        .stream()
                        .map(AnnotationTypeFactory::from)
                        .toArray(AnnotationType[]::new));
    }

    /**
     * For given accessor method created corresponding field using given naming convention for field
     *
     * @throws IllegalArgumentException when given {@link ExecutableElement} is not getter
     */
    public static Field fromGetters(ExecutableElement method, Function<String, String> namingConvention) {
        if (!ACCESSOR.test(method)) {
            throw new IllegalArgumentException("ExecutableElement is not getter");
        }

        return new Field(namingConvention.apply(valueOf(method.getSimpleName())), TypeFactory.from(method.getReturnType()));
    }

    /**
     * For given accessor method created corresponding field using name of method for field
     *
     * @throws IllegalArgumentException when given {@link ExecutableElement} is not getter
     */
    public static Field fromGetters(ExecutableElement method) {
        return fromGetters(method, Function.identity());
    }
}
