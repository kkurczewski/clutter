package io.clutter.model.type;

import java.util.Objects;

import static io.clutter.model.type.BoundedWildcardType.BoundaryType.EXTENDS;
import static io.clutter.model.type.BoundedWildcardType.BoundaryType.SUPER;

public class WildcardType extends BoxedType {

    public static final WildcardType ANY = WildcardType.alias("?");
    public static final WildcardType T = WildcardType.alias("T");
    public static final WildcardType U = WildcardType.alias("U");
    public static final WildcardType V = WildcardType.alias("V");
    public static final WildcardType R = WildcardType.alias("R");

    private final String alias;

    private WildcardType(String alias) {
        super(Object.class);
        this.alias = alias;
    }

    WildcardType(String alias, Class<?> type) {
        super(type);
        this.alias = alias;
    }

    public static WildcardType alias(String alias) {
        return new WildcardType(alias);
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

    public BoundedWildcardType extend(Class<?> exactType) {
        return new BoundedWildcardType(alias, EXTENDS, BoxedType.of(exactType));
    }

    public BoundedWildcardType extend(BoxedType containerType) {
        return new BoundedWildcardType(alias, EXTENDS, containerType);
    }

    public BoundedWildcardType subclass(Class<?> exactType) {
        return new BoundedWildcardType(alias, SUPER, BoxedType.of(exactType));
    }

    public BoundedWildcardType subclass(BoxedType containerType) {
        return new BoundedWildcardType(alias, SUPER, containerType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WildcardType that = (WildcardType) o;
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
