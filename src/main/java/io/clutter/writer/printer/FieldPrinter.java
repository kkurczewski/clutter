package io.clutter.writer.printer;

import io.clutter.model.field.Field;
import io.clutter.model.field.modifiers.FieldTrait;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static io.clutter.writer.printer.PrinterUtils.*;
import static java.util.stream.Collectors.toList;

public class FieldPrinter {

    private static final String SEPARATOR = " ";

    private final TypePrinter typePrinter;
    private final AnnotationPrinter annotationPrinter;

    public FieldPrinter(TypePrinter typePrinter, AnnotationPrinter annotationPrinter) {
        this.typePrinter = typePrinter;
        this.annotationPrinter = annotationPrinter;
    }

    public FieldPrinter(TypePrinter typePrinter) {
        this(typePrinter, new AnnotationPrinter(typePrinter));
    }

    @SuppressWarnings("CollectionAddAllCanBeReplacedWithConstructor")
    public List<String> print(Field field) {
        List<String> lines = new LinkedList<>();

        lines.addAll(annotations(field));
        lines.add(fieldDeclaration(field) + fieldValue(field));

        return lines;
    }

    private List<String> annotations(Field field) {
        return field.getAnnotations()
                .stream()
                .map(annotationPrinter::print)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private String fieldValue(Field field) {
        return field.getValue().map(" = "::concat).orElse("") + ";";
    }

    private String fieldDeclaration(Field field) {
        return joinNonBlank(List.of(
                field.getVisibility().toString(),
                traits(field.getTraits()),
                typePrinter.print(field.getType()),
                field.getName()
        ), SEPARATOR);
    }

    private String traits(List<FieldTrait> traits) {
        return joinNonBlank(traits, SEPARATOR);
    }
}
