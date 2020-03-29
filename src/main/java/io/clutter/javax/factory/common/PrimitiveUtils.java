package io.clutter.javax.factory.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final public class PrimitiveUtils {

    private final static Map<Class<?>, Class<?>> primitiveTypesMappings = Map.of(
            Short.class, short.class,
            Integer.class, int.class,
            Long.class, long.class,
            Float.class, float.class,
            Double.class, double.class,
            Byte.class, byte.class,
            Character.class, char.class,
            Boolean.class, boolean.class,
            Void.class, void.class
    );

    public static Class<?> toUnboxedPrimitive(Class<?> boxedPrimitive) {
        return primitiveTypesMappings.get(boxedPrimitive);
    }

    public static Set<Class<?>> primitives() {
        return new HashSet<>(primitiveTypesMappings.values());
    }

    public static Set<Class<?>> boxedPrimitives() {
        return new HashSet<>(primitiveTypesMappings.keySet());
    }
}
