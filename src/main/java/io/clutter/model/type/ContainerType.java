package io.clutter.model.type;

import io.clutter.common.Varargs;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

final public class ContainerType extends BoxedType {

    private final List<? extends BoxedType> genericValues;

    private ContainerType(Class<?> container, List<BoxedType> genericValues) {
        super(container);
        this.genericValues = genericValues;
    }

    public static ContainerType of(Class<?> container) {
        return new ContainerType(container, emptyList());
    }

    public static ContainerType of(Class<?> container, BoxedType type, BoxedType... more) {
        return new ContainerType(container, Varargs.concat(type, more));
    }

    public static ContainerType of(Class<?> container, PrimitiveType type, PrimitiveType... more) {
        return new ContainerType(container, Varargs
                .concat(type, more)
                .stream()
                .map(PrimitiveType::boxed)
                .collect(toList()));
    }

    public static ContainerType of(Class<?> container, Class<?> type, Class<?>... more) {
        return new ContainerType(container, Varargs
                .concat(type, more)
                .stream()
                .map(BoxedType::of)
                .collect(toList()));
    }

    public static ContainerType of(Class<?> container, BoxedType[] more) {
        return new ContainerType(container, List.of(more));
    }

    public static ContainerType listOf(BoxedType type) {
        return genericOf(List.class, type);
    }

    public static ContainerType listOf(PrimitiveType type) {
        return genericOf(List.class, type.boxed());
    }

    public static ContainerType listOf(Class<?> type) {
        return listOf(BoxedType.of(type));
    }

    public static ContainerType setOf(BoxedType type) {
        return genericOf(Set.class, type);
    }

    public static ContainerType setOf(PrimitiveType type) {
        return genericOf(Set.class, type.boxed());
    }

    public static ContainerType setOf(Class<?> type) {
        return setOf(BoxedType.of(type));
    }

    public static ContainerType mapOf(BoxedType keyType, BoxedType valueType) {
        return genericOf(Map.class, keyType, valueType);
    }

    public static ContainerType mapOf(PrimitiveType keyType, PrimitiveType valueType) {
        return genericOf(Map.class, keyType.boxed(), valueType.boxed());
    }

    public static ContainerType mapOf(Class<?> keyType, Class<?> valueType) {
        return mapOf(BoxedType.of(keyType), BoxedType.of(valueType));
    }

    public static ContainerType genericOf(Class<?> container, BoxedType... types) {
        return new ContainerType(container, List.of(types));
    }

    public List<? extends BoxedType> genericValues() {
        return genericValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ContainerType that = (ContainerType) o;
        return genericValues.equals(that.genericValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), genericValues);
    }

    @Override
    public String toString() {
        return format("%s{%s}", type, genericValues);
    }
}
