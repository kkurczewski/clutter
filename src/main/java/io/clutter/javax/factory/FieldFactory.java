package io.clutter.javax.factory;

import io.clutter.javax.factory.common.PojoNamingConvention;
import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.field.Field;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import static io.clutter.javax.extractor.Filters.*;
import static java.lang.String.valueOf;

final public class FieldFactory {

    public static Field from(VariableElement field) {
        if (!FIELD.test(field)) {
            throw new IllegalArgumentException("VariableElement is not field");
        }
        return new Field(valueOf(field.getSimpleName()), TypeFactory.from(field.asType()))
                .setAnnotations(field
                        .getAnnotationMirrors()
                        .stream()
                        .map(AnnotationTypeFactory::from)
                        .toArray(AnnotationType[]::new));
    }

    /**
     * @throws IllegalArgumentException when given {@link ExecutableElement} is not getter
     */
    public static Field property(ExecutableElement method, PojoNamingConvention namingConvention) {
        if (!METHOD.test(method)) {
            throw new IllegalArgumentException("ExecutableElement is not method");
        }
        if (!ACCESSOR.test(method)) {
            throw new IllegalArgumentException("ExecutableElement is not getter");
        }

        return new Field(namingConvention.variable(valueOf(method.getSimpleName())), TypeFactory.from(method.getReturnType()));
    }
}
