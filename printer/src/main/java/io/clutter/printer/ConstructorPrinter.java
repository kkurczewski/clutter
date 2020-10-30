package io.clutter.printer;

import io.clutter.model.common.Expression;
import io.clutter.model.common.Visibility;
import io.clutter.model.ctor.Constructor;
import io.clutter.model.type.GenericType;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

final public class ConstructorPrinter {

    private final TypePrinter typePrinter;
    private final AnnotationPrinter annotationPrinter;

    public ConstructorPrinter() {
        var packagePrinter = new PackagePrinter();
        this.typePrinter = new TypePrinter(packagePrinter);
        this.annotationPrinter = new AnnotationPrinter();
    }

    public List<String> print(Constructor constructor, String className) {
        var annotations = annotationPrinter.print(constructor.getAnnotations());
        var visibility = visibility(constructor.getVisibility());

        var genericTypes = constructor.getGenericTypes();
        var generics = !genericTypes.isEmpty()
            ? genericTypes.stream().map(GenericType::toString).collect(joining(", ", "<", ">"))
            : "";

        var arguments = constructor
            .getArguments()
            .stream()
            .map(argument -> argument.getValue().accept(typePrinter) + " " + argument.getName())
            .collect(joining(", ", className + "(", ")"));
        var bodyContent = printBody(constructor.getBody());

        var header = Stream.of(visibility, generics, arguments)
            .filter(headerPart -> !headerPart.isBlank())
            .collect(joining(" "));

        var lines = new LinkedList<>(annotations);
        lines.add(header + bodyContent.removeFirst());
        lines.addAll(bodyContent);

        return lines;
    }

    private String visibility(Visibility visibility) {
        if (visibility != null) {
            return visibility.name().toLowerCase();
        }
        return "";
    }

    private LinkedList<String> printBody(List<Expression> expressions) {
        var lines = expressions
            .stream()
            .map(Expression::asString)
            .collect(toCollection(LinkedList::new));

        lines.addFirst(" {");
        lines.addLast("}");

        return lines;
    }
}