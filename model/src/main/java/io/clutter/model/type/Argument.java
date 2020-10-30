package io.clutter.model.type;

import io.clutter.model.annotation.AnnotationT;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final public class Argument {

    private final List<AnnotationT> annotations = new LinkedList<>();
    private final String name;
    private final Type value;

    private Argument(String name, Type value) {
        this.name = name;
        this.value = value;
    }

    public static Argument of(String name, Type type) {
        return new Argument(name, type);
    }

    public Argument setAnnotations(Consumer<List<AnnotationT>> mutation) {
        mutation.accept(annotations);
        return this;
    }

    public String getName() {
        return name;
    }

    public Type getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Argument param = (Argument) o;
        return name.equals(param.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + '{' + value + '}';
    }
}
