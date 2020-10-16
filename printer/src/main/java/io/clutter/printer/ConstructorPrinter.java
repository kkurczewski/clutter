package io.clutter.printer;

import io.clutter.model.constructor.Constructor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static io.clutter.printer.PrinterUtils.nested;
import static java.util.stream.Collectors.toList;

final class ConstructorPrinter {

    public static final String SEPARATOR = " ";

    private final TypePrinter typePrinter;
    private final AnnotationPrinter annotationPrinter;
    private final ParamPrinter paramPrinter;

    public ConstructorPrinter(TypePrinter typePrinter) {
        this.typePrinter = typePrinter;
        this.annotationPrinter = new AnnotationPrinter(typePrinter);
        this.paramPrinter = new ParamPrinter(typePrinter);
    }

    @SuppressWarnings("CollectionAddAllCanBeReplacedWithConstructor")
    public List<String> print(Constructor constructor) {
        List<String> lines = new LinkedList<>();

        lines.addAll(annotations(constructor));
//        lines.add(joinNonBlank(List.of(
//                constructor.getVisibility().toString(),
//                typePrinter.printGenerics(constructor.getGenericParameters()),
//                constructor.getClassName() + "(" + paramPrinter.print(constructor.getParams()) + ") {"
//        ), SEPARATOR));

        lines.addAll(body(constructor));
        lines.add("}");

        return lines;
    }

    private List<String> annotations(Constructor constructor) {
        return constructor.getAnnotations()
                .stream()
                .map(annotationPrinter::print)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private List<String> body(Constructor constructor) {
        return nested(lines -> lines.addAll(constructor.getBody()));
    }
}
