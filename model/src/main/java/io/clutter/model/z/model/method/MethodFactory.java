package io.clutter.model.z.model.method;

import io.clutter.model.z.model.common.Expression;
import io.clutter.model.z.model.common.Trait;
import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.param.Argument;
import io.clutter.model.type.GenericType;
import io.clutter.model.type.Type;
import io.clutter.model.z.collection.*;
import io.clutter.model.z.model.common.Visibility;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Static factories for {@link Method}.
 */
class MethodFactory {

    MethodFactory() {
    }

    public static Method ofGeneric(
            List<AnnotationType> annotations,
            Visibility visibility, List<Trait> traits,
            String name,
            Type returnValue,
            List<Argument> args,
            List<Expression> body,
            List<GenericType> genericTypes
    ) {
        return new Method(
                new SafeList<>(annotations),
                visibility,
                new SafeList<>(traits),
                name,
                returnValue,
                new SafeList<>(args),
                new SafeList<>(body),
                new SafeList<>(genericTypes)
        );
    }

    public static Method of(
            List<AnnotationType> annotations,
            Visibility visibility, List<Trait> traits,
            String name,
            Type returnValue,
            List<Argument> args,
            List<Expression> body
    ) {
        return ofGeneric(annotations, visibility, traits, name, returnValue, args, body, emptyList());
    }

    public static Method prototype(
            List<AnnotationType> annotations,
            Visibility visibility, List<Trait> traits,
            String name,
            Type returnValue,
            List<Argument> args
    ) {
        return of(annotations, visibility, traits, name, returnValue, args, emptyList());
    }

    public static Method getterPrototype(List<AnnotationType> annotations, Visibility visibility, List<Trait> traits, String name, Type returnValue) {
        return prototype(annotations, visibility, traits, name, returnValue, emptyList());
    }

    public static Method getterPrototype(Visibility visibility, List<Trait> traits, String name, Type returnValue) {
        return prototype(emptyList(), visibility, traits, name, returnValue, emptyList());
    }

    public static Method setterPrototype(List<AnnotationType> annotations, Visibility visibility, List<Trait> traits, String name, List<Argument> args) {
        return prototype(annotations, visibility, traits, name, Type.of(void.class), args);
    }

    public static Method setterPrototype(Visibility visibility, List<Trait> traits, String name, List<Argument> args) {
        return prototype(emptyList(), visibility, traits, name, Type.of(void.class), args);
    }
}