package io.clutter.model.z.model.field;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.type.Type;
import io.clutter.model.z.collection.SafeList;
import io.clutter.model.z.model.common.Expression;
import io.clutter.model.z.model.common.Trait;
import io.clutter.model.z.model.common.Visibility;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Static factories for {@link Field}.
 */
class FieldFactory {

    FieldFactory() {
    }

    public static Field initializedMember(
            List<AnnotationType> annotations,
            Visibility visibility, List<Trait> traits,
            String name,
            Type type,
            Expression expression
    ) {
        return new Field(new SafeList<>(annotations), visibility, new SafeList<>(traits), name, type, expression);
    }

    public static Field annotatedMember(List<AnnotationType> annotations, Visibility visibility, List<Trait> traits, String name, Type returnValue) {
        return initializedMember(annotations, visibility, traits, name, returnValue, Expression.empty());
    }

    public static Field prototypeMember(Visibility visibility, List<Trait> traits, String name, Type returnValue) {
        return annotatedMember(emptyList(), visibility, traits, name, returnValue);
    }
}