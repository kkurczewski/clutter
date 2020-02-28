package io.clutter.writer.model.method.modifiers;

public enum MethodTrait {
    ABSTRACT("abstract"), STATIC("static"), FINAL("final");

    private final String value;

    MethodTrait(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
