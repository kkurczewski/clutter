package io.clutter.model.method.modifiers;

public enum MethodVisibility {
    PUBLIC("public"), PACKAGE_PRIVATE(""), PROTECTED("protected"), PRIVATE("private");

    private final String value;

    MethodVisibility(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
