package io.clutter.model.z.model.clazz;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.type.GenericType;
import io.clutter.model.z.collection.SafeList;
import io.clutter.model.z.model.common.Trait;
import io.clutter.model.z.model.common.Visibility;
import io.clutter.model.z.model.ctor.Constructor;
import io.clutter.model.z.model.field.Field;
import io.clutter.model.z.model.method.Method;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Static factories for {@link Clazz}.
 */
class ClazzFactory {

    ClazzFactory() {
    }

    public static Clazz ofGeneric(
            List<AnnotationType> annotations,
            Visibility visibility,
            List<Trait> traits,
            String name,
            List<Field> fields,
            List<Constructor> constructors,
            List<Method> methods,
            List<GenericType> genericTypes
    ) {
        return new Clazz(
                new SafeList<>(annotations),
                visibility,
                new SafeList<>(traits),
                name,
                new SafeList<>(fields),
                new SafeList<>(constructors),
                new SafeList<>(methods),
                new SafeList<>(genericTypes)
        );
    }

    public static Clazz of(
            List<AnnotationType> annotations,
            Visibility visibility,
            List<Trait> traits,
            String name,
            List<Field> fields,
            List<Constructor> constructors,
            List<Method> methods
    ) {
        return Clazz.ofGeneric(annotations, visibility, traits, name, fields, constructors, methods, emptyList());
    }

    public static Clazz of(
            Visibility visibility,
            List<Trait> traits,
            String name,
            List<Field> fields,
            List<Constructor> constructors,
            List<Method> methods
    ) {
        return Clazz.ofGeneric(emptyList(), visibility, traits, name, fields, constructors, methods, emptyList());
    }
}