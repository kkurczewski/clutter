package io.clutter.model.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

final public class Varargs {

    @SafeVarargs
    public static <T> List<T> concat(T value, T... more) {
        List<T> values = new LinkedList<>();
        values.add(value);
        Collections.addAll(values, more);
        return values;
    }
}
