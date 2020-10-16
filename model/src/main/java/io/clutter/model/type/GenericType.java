package io.clutter.model.type;

import java.util.Objects;

import static io.clutter.model.type.BoundedGenericType.BoundaryType.EXTENDS;
import static io.clutter.model.type.BoundedGenericType.BoundaryType.SUPER;

public class GenericType extends BoxedType {

    public static final GenericType ANY = GenericType.alias("?");
    public static final GenericType T = GenericType.alias("T");
    public static final GenericType U = GenericType.alias("U");
    public static final GenericType V = GenericType.alias("V");
    public static final GenericType R = GenericType.alias("R");

    private final String alias;

    private GenericType(String alias) {
        super(Object.class);
        this.alias = alias;
    }

    GenericType(String alias, Class<?> type) {
        super(type);
        this.alias = alias;
    }

    public static GenericType alias(String alias) {
        return new GenericType(alias);
    }

    /**
     * Returns approximate class represented by this wildcard
     *
     * @return T type if wildcard is bounded (i.e. ? super T, ? extends T) or {@link Object} otherwise
     */
    @Override
    public Class<?> getType() {
        return super.getType();
    }

    public String getAlias() {
        return alias;
    }

    public BoundedGenericType extend(Class<?> exactType) {
        return new BoundedGenericType(alias, EXTENDS, BoxedType.of(exactType));
    }

    public BoundedGenericType extend(BoxedType containerType) {
        return new BoundedGenericType(alias, EXTENDS, containerType);
    }

    public BoundedGenericType subclass(Class<?> exactType) {
        return new BoundedGenericType(alias, SUPER, BoxedType.of(exactType));
    }

    public BoundedGenericType subclass(BoxedType containerType) {
        return new BoundedGenericType(alias, SUPER, containerType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GenericType that = (GenericType) o;
        return alias.equals(that.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), alias);
    }

    @Override
    public String toString() {
        return alias;
    }
}
