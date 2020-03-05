package io.clutter.writer.model.type;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Arrays.stream;

final public class WrappedType extends Type {

    private WrappedType(String value, String boxed) {
        super(value, boxed);
    }

    public static WrappedType generic(Class<?> wrapper, Type... types) {
        String[] rawTypes = stream(types).map(t -> t.boxed).toArray(String[]::new);
        return new WrappedType(formatGeneric(wrapper, rawTypes), formatGeneric(wrapper, rawTypes));
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
