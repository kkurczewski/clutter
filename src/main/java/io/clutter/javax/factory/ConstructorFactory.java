package io.clutter.javax.factory;

import io.clutter.javax.filter.Filters;
import io.clutter.writer.common.PojoNamingConvention;
import io.clutter.writer.model.constructor.Constructor;
import io.clutter.writer.model.param.Param;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.stream.Stream;

import static java.lang.String.valueOf;

final public class ConstructorFactory {

    public static Constructor fromVariableElements(VariableElement... fields) {
        Param[] params = Stream.of(fields)
                .map(field -> new Param(
                        valueOf(field.getSimpleName()),
                        valueOf(field.asType())))
                .toArray(Param[]::new);

        return new Constructor(params);
    }

    public static Constructor fromGetters(PojoNamingConvention namingConvention, ExecutableElement... methods) {
        Param[] params = Stream.of(methods)
                .filter(Filters.METHOD)
                .filter(Filters.ACCESSOR)
                .map(getter -> new Param(
                        namingConvention.variable(valueOf(getter.getSimpleName())),
                        valueOf(getter.getReturnType())))
                .toArray(Param[]::new);
        return new Constructor(params);
    }
}
