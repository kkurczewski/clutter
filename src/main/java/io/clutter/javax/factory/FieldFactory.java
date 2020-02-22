package io.clutter.javax.factory;

import io.clutter.writer.common.PojoNamingConvention;
import io.clutter.writer.model.field.Field;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import static io.clutter.javax.filter.Filters.*;

final public class FieldFactory {

    /**
     * @throws IllegalArgumentException when given {@link ExecutableElement} is not getter or setter
     */
    public static Field property(ExecutableElement method, PojoNamingConvention namingConvention) {
        if (!METHOD.test(method)) {
            throw new IllegalArgumentException("ExecutableElement is not method");
        }

        final TypeMirror type;
        if (ACCESSOR.test(method)) {
            type = method.getReturnType();
        } else if (SINGLE_ARG.test(method)) {
            type = method.getParameters().get(0).asType();
        } else {
            throw new IllegalArgumentException("ExecutableElement should has getter or setter");
        }
        return new Field(namingConvention.variable(String.valueOf(method.getSimpleName())), type.toString());
    }
}
