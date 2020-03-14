package io.clutter.model.annotation.param;

import java.lang.reflect.Array;
import java.util.Objects;

import static java.lang.String.*;

final public class AnnotationParam {

    private final String key;
    private final String value;
    private final Object rawValue;

    private AnnotationParam(String key, String value, Object rawValue) {
        this.key = key;
        this.value = value;
        this.rawValue = rawValue;
    }

    @Deprecated
    public static AnnotationParam ofRaw(String key, Object rawValue) {
        Class<?> type = rawValue.getClass();
        String stringValue = type.isArray() ? paramAsArray(rawValue) : paramAsString(rawValue);
        return new AnnotationParam(key, stringValue, rawValue);
    }

    public static <T extends Enum<?>> AnnotationParam ofEnum(String key, T type) {
        return new AnnotationParam(key, paramAsString(type), type);
    }

    public static AnnotationParam ofClass(String key, Class<?> type) {
        return new AnnotationParam(key, paramAsString(type), type);
    }

    public static AnnotationParam ofShort(String key, short primitive) {
        return new AnnotationParam(key, paramAsString(primitive), primitive);
    }

    public static AnnotationParam ofInt(String key, int primitive) {
        return new AnnotationParam(key, paramAsString(primitive), primitive);
    }

    public static AnnotationParam ofLong(String key, long primitive) {
        return new AnnotationParam(key, paramAsString(primitive), primitive);
    }

    public static AnnotationParam ofFloat(String key, float primitive) {
        return new AnnotationParam(key, paramAsString(primitive), primitive);
    }

    public static AnnotationParam ofDouble(String key, double primitive) {
        return new AnnotationParam(key, paramAsString(primitive), primitive);
    }

    public static AnnotationParam ofBool(String key, boolean primitive) {
        return new AnnotationParam(key, paramAsString(primitive), primitive);
    }

    public static AnnotationParam ofChar(String key, char primitive) {
        return new AnnotationParam(key, paramAsString(primitive), primitive);
    }

    public static AnnotationParam ofByte(String key, byte primitive) {
        return new AnnotationParam(key, paramAsString(primitive), primitive);
    }

    public static AnnotationParam ofString(String key, String str) {
        return new AnnotationParam(key, paramAsString(str), str);
    }

    @SafeVarargs
    public static <T extends Enum<?>> AnnotationParam ofEnumArray(String key, T... enums) {
        return new AnnotationParam(key, paramAsArray(enums), enums);
    }

    public static AnnotationParam ofClassArray(String key, Class<?>... classes) {
        return new AnnotationParam(key, paramAsArray(classes), classes);
    }

    public static AnnotationParam ofShortArray(String key, short... primitives) {
        return new AnnotationParam(key, paramAsArray(primitives), primitives);
    }

    public static AnnotationParam ofIntArray(String key, int... primitives) {
        return new AnnotationParam(key, paramAsArray(primitives), primitives);
    }

    public static AnnotationParam ofLongArray(String key, long... primitives) {
        return new AnnotationParam(key, paramAsArray(primitives), primitives);
    }

    public static AnnotationParam ofFloatArray(String key, float... primitives) {
        return new AnnotationParam(key, paramAsArray(primitives), primitives);
    }

    public static AnnotationParam ofDoubleArray(String key, double... primitives) {
        return new AnnotationParam(key, paramAsArray(primitives), primitives);
    }

    public static AnnotationParam ofBoolArray(String key, boolean... primitives) {
        return new AnnotationParam(key, paramAsArray(primitives), primitives);
    }

    public static AnnotationParam ofCharArray(String key, char... primitives) {
        return new AnnotationParam(key, paramAsArray(primitives), primitives);
    }

    public static AnnotationParam ofByteArray(String key, byte... primitives) {
        return new AnnotationParam(key, paramAsArray(primitives), primitives);
    }

    public static AnnotationParam ofStringArray(String key, String... strings) {
        return new AnnotationParam(key, paramAsArray(strings), strings);
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

    private static String paramAsString(Object rawValue) {
        if (rawValue instanceof String) {
            return "\"" + rawValue + "\"";
        } else if (rawValue instanceof Enum) {
            return rawValue.getClass().getCanonicalName() + '.' + ((Enum<?>) rawValue).name();
        } else if (rawValue instanceof Class) {
            return ((Class<?>) rawValue).getCanonicalName() + ".class";
        }
        // assumed primitives
        return valueOf(rawValue);
    }

    private static String paramAsArray(Object rawArray) {
        final int length = Array.getLength(rawArray);
        String[] arr = new String[length];
        for (int i = 0; i < length; i++) {
            arr[i] = paramAsString(Array.get(rawArray, i));
        }
        return format("{%s}", join(", ", arr));
    }
}
