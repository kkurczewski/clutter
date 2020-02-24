package io.clutter.javax.factory;

import io.clutter.writer.common.PojoNamingConvention;
import io.clutter.writer.model.field.Field;

import javax.lang.model.element.ExecutableElement;

import static io.clutter.javax.filter.Filters.ACCESSOR;
import static io.clutter.javax.filter.Filters.METHOD;
import static java.lang.String.valueOf;

final public class FieldFactory {

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

        return new Field(namingConvention.variable(valueOf(method.getSimpleName())), valueOf(method.getReturnType()));
    }
}
