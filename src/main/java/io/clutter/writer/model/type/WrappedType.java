package io.clutter.writer.model.type;

import java.util.*;

import static java.lang.String.format;
import static java.lang.String.join;

final public class WrappedType extends Type {

    private WrappedType(String value, String boxed) {
        super(value, boxed);
    }

    public static WrappedType generic(Class<?> wrapper, Type type, Type... more) {
        List<Type> types = new ArrayList<>();
        types.add(type);
        Collections.addAll(types, more);
        return new WrappedType(
                formatGeneric(wrapper, types.stream().map(t -> t.value).toArray(String[]::new)),
                formatGeneric(wrapper, types.stream().map(t -> t.boxed).toArray(String[]::new))
        );
    }

    public static WrappedType listOf(Type type) {
        return generic(List.class, type);
    }

    public static WrappedType setOf(Type type) {
        return generic(Set.class, type);
    }

    public static WrappedType mapOf(Type keyType, Type valueType) {
        return generic(Map.class, keyType, valueType);
    }

    private static String formatGeneric(Class<?> wrapper, String[] rawType) {
        return format("%s<%s>", wrapper.getCanonicalName(), join(", ", rawType));
    }
}
