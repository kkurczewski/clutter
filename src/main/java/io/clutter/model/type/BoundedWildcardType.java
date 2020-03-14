package io.clutter.model.type;

import java.util.Objects;

final public class BoundedWildcardType extends WildcardType {

    public enum BoundaryType {
        EXTENDS, SUPER;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    private final BoxedType bound;
    private final BoundaryType boundaryKeyword;

    protected BoundedWildcardType(String alias, BoundaryType boundaryKeyword, BoxedType bound) {
        super(alias, bound.getType());
        this.boundaryKeyword = boundaryKeyword;
        this.bound = bound;
    }

    public BoundaryType getBoundaryKeyword() {
        return boundaryKeyword;
    }

    public BoxedType getBound() {
        return bound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BoundedWildcardType that = (BoundedWildcardType) o;
        return bound.equals(that.bound) &&
                boundaryKeyword == that.boundaryKeyword;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bound, boundaryKeyword);
    }

    @Override
    public String toString() {
        return alias + ' ' + boundaryKeyword + ' ' + bound.toString();
    }
}
