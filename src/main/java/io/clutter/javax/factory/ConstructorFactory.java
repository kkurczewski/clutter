package io.clutter.javax.factory;

import io.clutter.javax.filter.Filters;
import io.clutter.writer.common.PojoNamingConvention;
import io.clutter.writer.model.constructor.Constructor;
import io.clutter.writer.model.param.Params;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.stream.Stream;

import static java.lang.String.valueOf;

final public class ConstructorFactory {

    public static Constructor fromVariableElements(VariableElement... fields) {
        Params params = new Params();
        Stream.of(fields).forEach(field -> params.add(
                valueOf(field.getSimpleName()),
                valueOf(field.asType()))
        );
        return new Constructor(params);
    }

    public static Constructor fromGetters(PojoNamingConvention namingConvention, ExecutableElement... methods) {
        Params params = new Params();
        Stream.of(methods)
                .filter(Filters.METHOD)
                .filter(Filters.ACCESSOR)
                .forEach(getter -> params.add(
                        namingConvention.variable(valueOf(getter.getSimpleName())),
                        valueOf(getter.getReturnType()))
                );
        return new Constructor(params);
    }
}
