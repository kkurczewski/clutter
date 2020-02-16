package io.clutter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class TestAnnotations {

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

}