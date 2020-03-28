package io.clutter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class TestElements {

    public interface TestInterface<T> {}

    public static class TestClass<T> {}

    @Target(ElementType.TYPE)
    public @interface FooClass {

    }

    @Target(ElementType.TYPE)
    public @interface BarClass {

    }

    @Target(ElementType.FIELD)
    public @interface FooField {

    }

    @Target(ElementType.FIELD)
    public @interface BarField {

    }

    @Target(ElementType.METHOD)
    public @interface FooMethod {

    }

    @Target(ElementType.METHOD)
    public @interface BarMethod {

    }

    public @interface BarElement {

    }

    public enum TestEnum {
        FOO, BAR
    }

    public @interface Aggregate {
        int intValue();

        String stringValue();

        Class<?> classValue();

        TestEnum enumValue();

        TestElements.FooClass annotationValue();

        int[] intArray();

        String[] stringArray();

        Class<?>[] classArray();

        TestEnum[] enumArray();

        TestElements.FooClass[] annotationArray();
    }
}
