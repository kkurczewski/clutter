package io.clutter.common;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Varargs {

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> T[] concat(T value, T... more) {
        List<T> values = new LinkedList<>();
        values.add(value);
        Collections.addAll(values, more);
        return (T[]) values.toArray();
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends Annotation>[] concat(Class<? extends Annotation> value, Class<? extends Annotation>... more) {
        List<Class<? extends Annotation>> values = new LinkedList<>();
        values.add(value);
        Collections.addAll(values, more);
        return values.toArray(Class[]::new);
    }
}
