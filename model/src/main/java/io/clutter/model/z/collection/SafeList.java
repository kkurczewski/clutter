package io.clutter.model.z.collection;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class SafeList<T> {

    private final List<T> values;

    public SafeList(List<T> values) {
        this.values = new LinkedList<>(values);
    }

    public SafeList() {
        this(new LinkedList<>());
    }

    public void modify(Consumer<List<T>> mutation) {
        mutation.accept(values);
    }

    public List<T> getValues() {
        return List.copyOf(values);
    }
}