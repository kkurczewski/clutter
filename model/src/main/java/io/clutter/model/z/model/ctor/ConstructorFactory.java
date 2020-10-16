package io.clutter.model.z.model.ctor;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.param.Argument;
import io.clutter.model.type.GenericType;
import io.clutter.model.type.Type;
import io.clutter.model.z.collection.SafeList;
import io.clutter.model.z.model.common.Expression;
import io.clutter.model.z.model.common.Visibility;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Static factories for {@link Constructor}.
 */
class ConstructorFactory {

    ConstructorFactory() {
    }

    public static Constructor ofGeneric(
            List<AnnotationType> annotations,
            Visibility visibility,
            Type returnValue,
            List<Argument> args,
            List<Expression> body,
            List<GenericType> genericTypes
    ) {
        return new Constructor(
                new SafeList<>(annotations),
                visibility,
                returnValue,
                new SafeList<>(args),
                new SafeList<>(body),
                new SafeList<>(genericTypes)
        );
    }

    public static Constructor of(
            List<AnnotationType> annotations,
            Visibility visibility,
            Type returnValue,
            List<Argument> args,
            List<Expression> body
    ) {
        return Constructor.ofGeneric(annotations, visibility, returnValue, args, body, emptyList());
    }

    public static Constructor noArgCtor(
            List<AnnotationType> annotations,
            Visibility visibility,
            Type returnValue,
            List<Expression> body
    ) {
        return Constructor.ofGeneric(annotations, visibility, returnValue, emptyList(), body, emptyList());
    }
}