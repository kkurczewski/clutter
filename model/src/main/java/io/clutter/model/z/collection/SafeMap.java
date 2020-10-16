package io.clutter.model.z.collection;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SafeMap<T, U> {

    private final Map<T, U> values;

    public SafeMap(Map<T, U> values) {
        this.values = new LinkedHashMap<>(values);
    }

    public SafeMap() {
        this(new LinkedHashMap<>());
    }

    public void modify(Consumer<Map<T, U>> mutation) {
        mutation.accept(values);
    }

    public Map<T, U> getValues() {
        return new LinkedHashMap<>(values);
    }
}