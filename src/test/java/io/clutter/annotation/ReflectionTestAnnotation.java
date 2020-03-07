package io.clutter.annotation;

import io.clutter.TestElements;

public @interface ReflectionTestAnnotation {

    short shortValue() default 0;
    int intValue() default 0;
    long longValue() default 0;
    float floatValue() default 0.0f;
    double doubleValue() default 0.0;

    boolean boolValue() default false;

    byte byteValue() default '\0';
    char charValue() default '\0';

    String stringValue() default "";
    Class<?> classValue() default Object.class;
    TestElements.TestEnum enumValue() default TestElements.TestEnum.BAR;

    short[] shortArray() default {};
    int[] intArray() default {};
    long[] longArray() default {};
    float[] floatArray() default {};
    double[] doubleArray() default {};

    boolean[] boolArray() default {};

    byte[] byteArray() default {};
    char[] charArray() default {};

    String[] stringArray() default {};
    Class<?>[] classArray() default {};
    TestElements.TestEnum[] enumArray() default {};
}