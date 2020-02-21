package io.clutter.writer.constructor;

import io.clutter.writer.param.Params;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.Collection;
import java.util.stream.Stream;

final public class ConstructorFactory {

    /**
     * Creates constructor from array of {@link ExecutableElement}
     */
    static public Constructor fromExecutableElements(ExecutableElement... methods) {
        return fromFields(Stream.of(methods)
                .map(ExecutableElement::getParameters)
                .flatMap(Collection::stream)
                .toArray(VariableElement[]::new));
    }

    /**
     * Creates constructor from array of {@link VariableElement}
     */
    static public Constructor fromFields(VariableElement... fields) {
        Params params = new Params();
        Stream.of(fields).forEach(a -> params.add(a.getSimpleName().toString(), a.asType().toString()));
        return new Constructor(params);
    }
}
