package io.clutter.javax.factory;

import io.clutter.javax.factory.types.TypeFactory;
import io.clutter.model.param.Argument;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import static java.lang.String.valueOf;

final public class ParamFactory {

    public static Argument[] from(ExecutableElement executableElement) {
        return executableElement.getParameters()
                .stream()
                .map(ParamFactory::from)
                .toArray(Argument[]::new);
    }

    private static Argument from(VariableElement field) {
        return new Argument(valueOf(field.getSimpleName()), TypeFactory.from(field.asType()));
    }
}
