package io.clutter.model.type;

/**
 * Dynamic type provides {@link Type}-alias for classes which
 * {@link Class} are not available in compile time
 */
final public class DynamicType implements Type {

    private String name;

    private DynamicType(String name) {
        this.name = name;
    }

    public static DynamicType of(String simpleName) {
        return new DynamicType(simpleName);
    }

    public Class<?> getType() {
        throw new DynamicTypeException(name);
    }

    public String getName() {
        return name;
    }

    public static class DynamicTypeException extends RuntimeException {
        private DynamicTypeException(String name) {
            super("Type with alias `" + name + "` is dynamic, can't return Class instance!");
        }
    }
}
