package io.clutter.javax.factory;

import io.clutter.javax.factory.common.PojoNamingConvention;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.param.Param;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.stream.Stream;

import static io.clutter.javax.extractor.Filters.ACCESSOR;
import static io.clutter.javax.extractor.Filters.FIELD;
import static java.lang.String.valueOf;
import static java.util.Arrays.stream;
import static java.util.function.Predicate.not;

final public class ConstructorFactory {

    public static Constructor fromVariableElements(VariableElement... fields) {
        stream(fields)
                .filter(not(FIELD))
                .findAny()
                .ifPresent(nonField -> {
                    throw new IllegalArgumentException("VariableElement is not field");
                });

        Param[] params = Stream.of(fields)
                .map(field -> Param.of(
                        valueOf(field.getSimpleName()),
                        TypeFactory.from(field.asType())))
                .toArray(Param[]::new);

        return new Constructor(params);
    }

    public static Constructor fromGetters(PojoNamingConvention namingConvention, ExecutableElement... methods) {
        stream(methods)
                .filter(not(ACCESSOR))
                .findAny()
                .ifPresent(nonField -> {
                    throw new IllegalArgumentException("ExecutableElement is not getter");
                });

        Param[] params = Stream.of(methods)
                .map(getter -> Param.of(
                        namingConvention.variable(valueOf(getter.getSimpleName())),
                        TypeFactory.from(getter.getReturnType())))
                .toArray(Param[]::new);
        return new Constructor(params);
    }
}
