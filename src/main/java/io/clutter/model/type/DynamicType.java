package io.clutter.model.type;

import java.util.Objects;

/**
 * Dynamic type allows to use classes not available at compile time (e.g. generated) at expense of runtime safety
 */
final public class DynamicType extends BoxedType {

    private final String name;

    private DynamicType(String name) {
        super(null);
        this.name = name;
    }

    public static DynamicType of(String name) {
        return new DynamicType(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to get type, consider using DynamicType::getName instead", e);
        }
    }

    @Override
    public String toString() {
        return "DynamicType{" + name + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DynamicType dynamicType = (DynamicType) o;
        return name.equals(dynamicType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
