package io.clutter.javax.factory;

import io.clutter.model.type.Argument;

import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

final public class JxArgumentAdapter {

    private JxArgumentAdapter() {
    }

    public static List<Argument> from(List<? extends VariableElement> arguments) {
        return arguments
                .stream()
                .map(JxArgumentAdapter::from)
                .collect(Collectors.toList());
    }

    public static Argument from(VariableElement field) {
        return Argument.of(valueOf(field.getSimpleName()), JxTypeAdapter.from(field.asType()));
    }
}
