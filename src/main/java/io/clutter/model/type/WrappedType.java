package io.clutter.model.type;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.clutter.common.Varargs.concat;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Arrays.stream;

final public class WrappedType extends Type {

    private WrappedType(String value, String boxed) {
        super(value, boxed);
    }

    public static WrappedType generic(Class<?> wrapper) {
        String value = formatGeneric(wrapper, new String[]{});
        return new WrappedType(value, value);
    }

    public static WrappedType generic(Class<?> wrapper, Type type, Type... more) {
        String[] rawTypes = concat(type, more)
                .stream()
                .map(t -> t.boxed)
                .toArray(String[]::new);
        return new WrappedType(formatGeneric(wrapper, rawTypes), formatGeneric(wrapper, rawTypes));
    }

    public static WrappedType generic(Class<?> wrapper, Class<?> type, Class<?>... more) {
        return generic(wrapper, Type.from(type), stream(more)
                .map(Type::from)
                .toArray(Type[]::new));
    }

    public static WrappedType listOf(Type type) {
        return generic(List.class, type);
    }

    public static WrappedType listOf(Class<?> type) {
        return listOf(Type.from(type));
    }

    public static WrappedType setOf(Type type) {
        return generic(Set.class, type);
    }

    public static WrappedType setOf(Class<?> type) {
        return setOf(Type.from(type));
    }

    public static WrappedType mapOf(Type keyType, Type valueType) {
        return generic(Map.class, keyType, valueType);
    }

    public static WrappedType mapOf(Class<?> keyType, Class<?> valueType) {
        return mapOf(Type.from(keyType), Type.from(valueType));
    }

    private static String formatGeneric(Class<?> wrapper, String[] rawType) {
        return format("%s<%s>", wrapper.getCanonicalName(), join(", ", rawType));
    }
}
