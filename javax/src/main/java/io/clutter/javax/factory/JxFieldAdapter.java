package io.clutter.javax.factory;

import io.clutter.model.field.Field;

import javax.lang.model.element.VariableElement;

import static java.lang.String.valueOf;

final public class JxFieldAdapter {

    private JxFieldAdapter() {
    }

    public static Field from(VariableElement field) {
        return new Field()
            .setName(valueOf(field.getSimpleName()))
            .setType(JxTypeAdapter.from(field.asType()))
            .setAnnotations(annotations -> annotations.addAll(JxAnnotationAdapter.from(field)));
    }
}
