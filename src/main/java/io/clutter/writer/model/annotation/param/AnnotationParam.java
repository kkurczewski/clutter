package io.clutter.writer.model.annotation.param;

import java.util.List;
import java.util.Objects;

import static java.lang.String.valueOf;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

final public class AnnotationParam {

    private final String key;
    private final String value;
    private final Object rawValue;

    private AnnotationParam(String key, String value, Object rawValue) {
        this.key = key;
        this.value = value;
        this.rawValue = rawValue;
    }

    public static <T extends Enum<?>> AnnotationParam ofEnum(String key, T type) {
        return new AnnotationParam(key, type.getClass().getCanonicalName() + '.' + type.name(), type);
    }

    public static AnnotationParam ofClass(String key, Class<?> type) {
        return new AnnotationParam(key, type.getCanonicalName() + ".class", type);
    }

    public static AnnotationParam ofPrimitive(String key, Object primitive) {
        if (isNotPrimitive(primitive)) {
            throw new IllegalArgumentException("Value is not primitive");
        }
        return new AnnotationParam(key, valueOf(primitive), primitive);
    }

    public static AnnotationParam ofRaw(String key, Object rawValue) {
        return new AnnotationParam(key, valueOf(rawValue), rawValue);
    }

    public static AnnotationParam ofString(String key, String str) {
        return new AnnotationParam(key, "\"" + str + "\"", str);
    }

    @SafeVarargs
    public static <T extends Enum<?>> AnnotationParam ofEnumArray(String key, T... enums) {
        return new AnnotationParam(key, stream(enums)
                .map(enumType -> enumType.getClass().getCanonicalName() + '.' + enumType.name())
                .collect(joining(", ", "{", "}")), enums);
    }

    public static AnnotationParam ofClassArray(String key, Class<?>... classes) {
        return new AnnotationParam(key, stream(classes)
                .map(Class::getCanonicalName)
                .map(type -> type + ".class")
                .collect(joining(", ", "{", "}")), classes);
    }

    public static AnnotationParam ofPrimitiveArray(String key, Object... primitives) {
        stream(primitives)
                .filter(AnnotationParam::isNotPrimitive)
                .findAny()
                .ifPresent(nonPrimitive -> {
                    throw new IllegalArgumentException("Only primitives are allowed");
                });
        return new AnnotationParam(key, stream(primitives)
                .map(String::valueOf)
                .collect(joining(", ", "{", "}")), primitives);
    }

    public static AnnotationParam ofStringArray(String key, String... strings) {
        return new AnnotationParam(key, "{\"" + String.join("\", \"", strings) + "\"}", strings);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Object getRawValue() {
        return rawValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationParam that = (AnnotationParam) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return key + '{' + rawValue + '}';
    }

    private static boolean isNotPrimitive(Object object) {
        // Due to autoboxing this is only way to make sure that given object is primitive
        List<Class<?>> boxedPrimitives = List.of(Byte.class, Character.class,
                Short.class, Integer.class, Long.class,
                Float.class, Double.class,
                Boolean.class);
        return object == null || !boxedPrimitives.contains(object.getClass());
    }
}
