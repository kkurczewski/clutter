package io.clutter.model.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final public class PrimitiveUtils {

    private final static Map<Class<?>, Class<?>> primitiveTypesMappings = Map.of(
            short.class, Short.class,
            int.class, Integer.class,
            long.class, Long.class,
            float.class, Float.class,
            double.class, Double.class,
            byte.class, Byte.class,
            char.class, Character.class,
            boolean.class, Boolean.class,
            void.class, Void.class
    );

    /**
     * Returns boxed class, if passed class is primitive boxed version is returned,
     * otherwise original argument is returned
     */
    public static Class<?> toBoxed(Class<?> clazz) {
        return primitiveTypesMappings.getOrDefault(clazz, clazz);
    }

    public static Set<Class<?>> primitives() {
        return new HashSet<>(primitiveTypesMappings.keySet());
    }
}
