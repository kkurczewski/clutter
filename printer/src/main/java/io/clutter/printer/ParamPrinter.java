package io.clutter.printer;

final class ParamPrinter {

    private final TypePrinter typePrinter;

    public ParamPrinter(TypePrinter typePrinter) {
        this.typePrinter = typePrinter;
    }

//    public String print(Collection<Param> params) {
//        return params
//                .stream()
//                .map(param -> typePrinter.print(param.getValue()) + " " + param.getName())
//                .collect(joining(", "));
//    }
}
