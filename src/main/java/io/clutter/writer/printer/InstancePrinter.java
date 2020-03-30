package io.clutter.writer.printer;

import io.clutter.model.type.Type;

final public class InstancePrinter {

    private TypePrinter typePrinter;

    public InstancePrinter(TypePrinter typePrinter) {
        this.typePrinter = typePrinter;
    }

    public <T extends Type> String toString(T type) {
        return "new " + typePrinter.print(type) + "()";
    }
}
