package io.clutter.model.annotation.param;

import java.lang.annotation.Annotation;
import java.util.Objects;

import static java.lang.String.format;

final public class AnnotationValue {

    private final Object value;

    private AnnotationValue(Object value) {
        this.value = value;
    }

    public static <T extends Enum<?>> AnnotationValue ofEnum(T type) {
        return new AnnotationValue(type);
    }

    public static <T extends Annotation> AnnotationValue ofAnnotation(T type) {
        return new AnnotationValue(type);
    }

    public static AnnotationValue ofClass(Class<?> type) {
        return new AnnotationValue(type);
    }

    public static AnnotationValue ofString(String string) {
        return new AnnotationValue(string);
    }

    public static AnnotationValue ofShort(short primitive) {
        return new AnnotationValue(primitive);
    }

    public static AnnotationValue ofInt(int primitive) {
        return new AnnotationValue(primitive);
    }

    public static AnnotationValue ofLong(long primitive) {
        return new AnnotationValue(primitive);
    }

    public static AnnotationValue ofFloat(float primitive) {
        return new AnnotationValue(primitive);
    }

    public static AnnotationValue ofDouble(double primitive) {
        return new AnnotationValue(primitive);
    }

    public static AnnotationValue ofBool(boolean primitive) {
        return new AnnotationValue(primitive);
    }

    public static AnnotationValue ofChar(char primitive) {
        return new AnnotationValue(primitive);
    }

    public static AnnotationValue ofByte(byte primitive) {
        return new AnnotationValue(primitive);
    }

    @SafeVarargs
    public static <T extends Enum<?>> AnnotationValue ofEnumArray(T... enums) {
        return new AnnotationValue(enums);
    }

    @SafeVarargs
    public static <T extends Annotation> AnnotationValue ofAnnotationArray(T... type) {
        return new AnnotationValue(type);
    }

    public static AnnotationValue ofClassArray(Class<?>... classes) {
        return new AnnotationValue(classes);
    }

    public static AnnotationValue ofStringArray(String... strings) {
        return new AnnotationValue(strings);
    }

    public static AnnotationValue ofShortArray(short... primitives) {
        return new AnnotationValue(primitives);
    }

    public static AnnotationValue ofIntArray(int... primitives) {
        return new AnnotationValue(primitives);
    }

    public static AnnotationValue ofLongArray(long... primitives) {
        return new AnnotationValue(primitives);
    }

    public static AnnotationValue ofFloatArray(float... primitives) {
        return new AnnotationValue(primitives);
    }

    public static AnnotationValue ofDoubleArray(double... primitives) {
        return new AnnotationValue(primitives);
    }

    public static AnnotationValue ofBoolArray(boolean... primitives) {
        return new AnnotationValue(primitives);
    }

    public static AnnotationValue ofCharArray(char... primitives) {
        return new AnnotationValue(primitives);
    }

    public static AnnotationValue ofByteArray(byte... primitives) {
        return new AnnotationValue(primitives);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationValue that = (AnnotationValue) o;
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