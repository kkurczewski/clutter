package io.clutter.writer.constructor.modifiers;

public enum ConstructorVisibility {
    PUBLIC("public"), PACKAGE_PRIVATE(""), PROTECTED("protected"), PRIVATE("private");

    private final String value;

    ConstructorVisibility(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
