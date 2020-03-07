package io.clutter.writer.model.type;

import java.util.Objects;

public class Type {

    protected final String value;
    protected final String boxed;

    Type(String value, String boxed) {
        // java.lang.* classes are imported by default
        this.value = value.replaceFirst("^java\\.lang\\.", "");
        this.boxed = boxed;
    }

    public static Type raw(String rawType) {
        return new Type(rawType, rawType);
    }

    public static Type from(Class<?> type) {
        return new Type(type.getCanonicalName(), type.getSimpleName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return value.equals(type.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }

}
