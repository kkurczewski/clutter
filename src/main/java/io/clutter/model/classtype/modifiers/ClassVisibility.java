package io.clutter.model.classtype.modifiers;

public enum ClassVisibility {
    PUBLIC("public"), PACKAGE_PRIVATE(""), PROTECTED("protected"), PRIVATE("private");

    private final String value;

    ClassVisibility(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
