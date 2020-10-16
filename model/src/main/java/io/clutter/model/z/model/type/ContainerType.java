package io.clutter.model.z.model.type;

import java.util.*;

import static io.clutter.model.util.Varargs.concat;

final public class ContainerType extends BoxedType {

    private final List<? extends BoxedType> genericValues;

    public ContainerType(Class<?> container, BoxedType... genericValues) {
        super(container);
        this.genericValues = Arrays.asList(genericValues);
    }

    @Deprecated
    public static ContainerType of(Class<?> container, BoxedType type, BoxedType... more) {
        return new ContainerType(container, concat(type, more).toArray(BoxedType[]::new));
    }

    @Deprecated
    public static ContainerType of(Class<?> container, Class<?> type, Class<?>... more) {
        return new ContainerType(container, concat(type, more)
                .stream()
                .map(BoxedType::of)
                .toArray(BoxedType[]::new));
    }

    public static ContainerType listOf(BoxedType type) {
        return genericOf(List.class, type);
    }

    public static ContainerType listOf(Class<?> type) {
        return listOf(BoxedType.of(type));
    }

    public static ContainerType setOf(BoxedType type) {
        return genericOf(Set.class, type);
    }

    public static ContainerType setOf(Class<?> type) {
        return setOf(BoxedType.of(type));
    }

    public static ContainerType mapOf(BoxedType keyType, BoxedType valueType) {
        return genericOf(Map.class, keyType, valueType);
    }

    public static ContainerType mapOf(Class<?> keyType, Class<?> valueType) {
        return mapOf(BoxedType.of(keyType), BoxedType.of(valueType));
    }

    public static ContainerType genericOf(Class<?> container, BoxedType... types) {
        return new ContainerType(container, types);
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

}
