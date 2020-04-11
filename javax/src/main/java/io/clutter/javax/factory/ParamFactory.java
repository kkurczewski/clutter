package io.clutter.javax.factory;

import io.clutter.javax.factory.types.TypeFactory;
import io.clutter.model.param.Param;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import static java.lang.String.valueOf;

final public class ParamFactory {

    public static Param[] from(ExecutableElement executableElement) {
        return executableElement.getParameters()
                .stream()
                .map(ParamFactory::from)
                .toArray(Param[]::new);
    }

    private static Param from(VariableElement field) {
        return new Param(valueOf(field.getSimpleName()), TypeFactory.from(field.asType()));
    }
}
