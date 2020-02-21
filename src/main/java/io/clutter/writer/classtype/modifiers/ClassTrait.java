package io.clutter.writer.classtype.modifiers;

public enum ClassTrait {
    ABSTRACT("abstract"), STATIC("static"), FINAL("final");

    private final String value;

    ClassTrait(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
