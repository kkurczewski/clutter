package io.clutter.model.annotation.param;

import java.lang.annotation.Annotation;
import java.util.Objects;

import static java.lang.String.format;

final public class AnnotationValue2 {

    private final String key;
    private final Object value;

    private AnnotationValue2(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static <T extends Enum<?>> AnnotationValue2 ofEnum(String key, T type) {
        return new AnnotationValue2(key, type);
    }

    public static <T extends Annotation> AnnotationValue2 ofAnnotation(String key, T type) {
        return new AnnotationValue2(key, type);
    }

    public static AnnotationValue2 ofClass(String key, Class<?> type) {
        return new AnnotationValue2(key, type);
    }

    public static AnnotationValue2 ofString(String key, String string) {
        return new AnnotationValue2(key, string);
    }

    public static AnnotationValue2 ofShort(String key, short primitive) {
        return new AnnotationValue2(key, primitive);
    }

    public static AnnotationValue2 ofInt(String key, int primitive) {
        return new AnnotationValue2(key, primitive);
    }

    public static AnnotationValue2 ofLong(String key, long primitive) {
        return new AnnotationValue2(key, primitive);
    }

    public static AnnotationValue2 ofFloat(String key, float primitive) {
        return new AnnotationValue2(key, primitive);
    }

    public static AnnotationValue2 ofDouble(String key, double primitive) {
        return new AnnotationValue2(key, primitive);
    }

    public static AnnotationValue2 ofBool(String key, boolean primitive) {
        return new AnnotationValue2(key, primitive);
    }

    public static AnnotationValue2 ofChar(String key, char primitive) {
        return new AnnotationValue2(key, primitive);
    }

    public static AnnotationValue2 ofByte(String key, byte primitive) {
        return new AnnotationValue2(key, primitive);
    }

    @SafeVarargs
    public static <T extends Enum<?>> AnnotationValue2 ofEnumArray(String key, T... enums) {
        return new AnnotationValue2(key, enums);
    }

    @SafeVarargs
    public static <T extends Annotation> AnnotationValue2 ofAnnotationArray(String key, T... type) {
        return new AnnotationValue2(key, type);
    }

    public static AnnotationValue2 ofClassArray(String key, Class<?>... classes) {
        return new AnnotationValue2(key, classes);
    }

    public static AnnotationValue2 ofStringArray(String key, String... strings) {
        return new AnnotationValue2(key, strings);
    }

    public static AnnotationValue2 ofShortArray(String key, short... primitives) {
        return new AnnotationValue2(key, primitives);
    }

    public static AnnotationValue2 ofIntArray(String key, int... primitives) {
        return new AnnotationValue2(key, primitives);
    }

    public static AnnotationValue2 ofLongArray(String key, long... primitives) {
        return new AnnotationValue2(key, primitives);
    }

    public static AnnotationValue2 ofFloatArray(String key, float... primitives) {
        return new AnnotationValue2(key, primitives);
    }

    public static AnnotationValue2 ofDoubleArray(String key, double... primitives) {
        return new AnnotationValue2(key, primitives);
    }

    public static AnnotationValue2 ofBoolArray(String key, boolean... primitives) {
        return new AnnotationValue2(key, primitives);
    }

    public static AnnotationValue2 ofCharArray(String key, char... primitives) {
        return new AnnotationValue2(key, primitives);
    }

    public static AnnotationValue2 ofByteArray(String key, byte... primitives) {
        return new AnnotationValue2(key, primitives);
    }

    public String getKey() {
        return key;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationValue2 that = (AnnotationValue2) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return format("AnnotationValue{value=%s}", value);
    }
}