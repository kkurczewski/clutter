package io.clutter.writer.printer;

import io.clutter.model.param.Param;

import java.util.Collection;

import static java.util.stream.Collectors.joining;

public class ParamPrinter {

    private final TypePrinter typePrinter;

    public ParamPrinter(TypePrinter typePrinter) {
        this.typePrinter = typePrinter;
    }

    public String print(Collection<Param> params) {
        return params
                .stream()
                .map(param -> typePrinter.print(param.getValue()) + " " + param.getName())
                .collect(joining(", "));
    }
}
