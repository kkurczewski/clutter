package io.clutter.printer;

import io.clutter.model.method.Method;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static io.clutter.printer.PrinterUtils.joinNonBlank;
import static io.clutter.printer.PrinterUtils.nested;

final class MethodPrinter {

    public static final String SEPARATOR = " ";

    private final TypePrinter typePrinter;
    private final AnnotationPrinter annotationPrinter;
    private final ParamPrinter paramPrinter;

    public MethodPrinter(TypePrinter typePrinter, AnnotationPrinter annotationPrinter, ParamPrinter paramPrinter) {
        this.typePrinter = typePrinter;
        this.annotationPrinter = annotationPrinter;
        this.paramPrinter = paramPrinter;
    }

    public MethodPrinter(TypePrinter typePrinter) {
        this(typePrinter, new AnnotationPrinter(typePrinter), new ParamPrinter(typePrinter));
    }

    @SuppressWarnings("CollectionAddAllCanBeReplacedWithConstructor")
    public List<String> print(Method method) {
        List<String> lines = new LinkedList<>();

        lines.addAll(annotations(method));
//        if (method.getTraits().contains(MethodTrait.ABSTRACT)) {
//            lines.add(methodDeclaration(method) + "(" + paramPrinter.print(method.getParams()) + ");");
//        } else {
//            lines.add(methodDeclaration(method) + "(" + paramPrinter.print(method.getParams()) + ") {");
//            lines.addAll(body(method));
//            lines.add("}");
//        }
        return lines;
    }

    private List<String> annotations(Method method) {
        return method.getAnnotations()
                .stream()
                .map(annotationPrinter::print)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<String> body(Method method) {
        return nested(lines -> lines.addAll(method.getBody()));
    }

    private String methodDeclaration(Method method) {
        return joinNonBlank(List.of(
                method.getVisibility().toString(),
                joinNonBlank(method.getTraits(), " "),
                typePrinter.printGenerics(method.getGenericParameters()),
                typePrinter.print(method.getReturnType()),
                method.getName()
        ), SEPARATOR);
    }
}
