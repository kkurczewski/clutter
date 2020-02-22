package io.clutter.javax.factory;

import io.clutter.writer.model.constructor.Constructor;
import io.clutter.writer.model.param.Params;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.Collection;
import java.util.stream.Stream;

final public class ConstructorFactory {

    public static Constructor fromExecutableElements(ExecutableElement... methods) {
        return fromVariableElement(Stream.of(methods)
                .map(ExecutableElement::getParameters)
                .flatMap(Collection::stream)
                .toArray(VariableElement[]::new));
    }

    public static Constructor fromVariableElement(VariableElement... fields) {
        Params params = new Params();
        Stream.of(fields).forEach(a -> params.add(a.getSimpleName().toString(), a.asType().toString()));
        return new Constructor(params);
    }
}
