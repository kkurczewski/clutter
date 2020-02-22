package io.clutter.writer.model.field.modifiers;

public enum FieldVisibility {
    PUBLIC("public"), PACKAGE_PRIVATE(""), PROTECTED("protected"), PRIVATE("private");

    private final String value;

    FieldVisibility(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
