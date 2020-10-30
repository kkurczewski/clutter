package io.clutter.printer;

import io.clutter.model.common.Expression;
import io.clutter.model.common.Visibility;
import io.clutter.model.field.Field;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNullElse;
import static java.util.stream.Collectors.joining;

final public class FieldPrinter {

    private final TypePrinter typePrinter;
    private final AnnotationPrinter annotationPrinter;

    public FieldPrinter() {
        var classPrinter = new PackagePrinter();
        this.typePrinter = new TypePrinter(classPrinter);
        this.annotationPrinter = new AnnotationPrinter();
    }

    public List<String> print(Field field) {
        var annotations = annotationPrinter.print(field.getAnnotations());
        var visibility = visibility(field.getVisibility());
        var traits = field.getTraits().stream().map(Enum::name).map(String::toLowerCase).collect(joining(" "));

        var type = field.getType().accept(typePrinter);
        var expression = requireNonNullElse(field.getExpression(), Expression.empty()).asString();

        var name = field.getName() + (!expression.isBlank() ? " = " + expression : "") + ";";

        var header = Stream.of(" ", visibility, traits, type, name)
            .filter(headerPart -> !headerPart.isBlank())
            .collect(joining(" "));

        var lines = new LinkedList<>(annotations);
        lines.add(header);

        return lines;
    }

    private String visibility(Visibility visibility) {
        if (visibility != null) {
            return visibility.name().toLowerCase();
        }
        return "";
    }
}