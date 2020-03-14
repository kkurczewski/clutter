package io.clutter.model.type;

import java.util.Objects;

public class Type {

    protected final Class<?> type;

    Type(Class<?> type) {
        this.type = type;
    }

    public static Type of(Class<?> type) {
        return new Type(type);
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type other = (Type) o;
        return type.equals(other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return type.getCanonicalName();
    }
}
