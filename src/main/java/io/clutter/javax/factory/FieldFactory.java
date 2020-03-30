package io.clutter.javax.factory;

import io.clutter.javax.factory.annotation.AnnotationFactory;
import io.clutter.javax.factory.types.TypeFactory;
import io.clutter.model.field.Field;

import javax.lang.model.element.VariableElement;

import static io.clutter.javax.extractor.Filters.FIELD;
import static java.lang.String.valueOf;

final public class FieldFactory {

    public static Field from(VariableElement field) {
        if (!FIELD.test(field)) {
            throw new IllegalArgumentException(field.getKind().name());
        }
        return new Field(valueOf(field.getSimpleName()), TypeFactory.from(field.asType()))
                .setAnnotations(AnnotationFactory.from(field));
    }
}
