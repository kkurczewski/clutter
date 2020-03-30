package io.clutter.writer.printer;

import io.clutter.model.type.BoxedType;

import java.util.Collection;

final public class GenericPrinter {

    private final TypePrinter typePrinter;

    public GenericPrinter(TypePrinter typePrinter) {
        this.typePrinter = typePrinter;
    }

    public String print(Collection<? extends BoxedType> boxedType) {
        return boxedType.stream()
                .map(typePrinter::print)
                .reduce((first, second) -> first + ", " + second)
                .map(type -> "<" + type + ">")
                .orElse("");
    }
}
