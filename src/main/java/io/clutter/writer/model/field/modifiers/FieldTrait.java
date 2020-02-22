package io.clutter.writer.model.field.modifiers;

public enum FieldTrait {
    STATIC("static"), FINAL("final");

    private final String value;

    FieldTrait(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
