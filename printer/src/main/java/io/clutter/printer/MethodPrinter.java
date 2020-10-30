package io.clutter.printer;

import io.clutter.model.common.Expression;
import io.clutter.model.common.Visibility;
import io.clutter.model.method.Method;
import io.clutter.model.type.GenericType;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

final public class MethodPrinter {

    private final TypePrinter typePrinter;
    private final AnnotationPrinter annotationPrinter;

    public MethodPrinter() {
        var classPrinter = new PackagePrinter();
        this.typePrinter = new TypePrinter(classPrinter);
        this.annotationPrinter = new AnnotationPrinter();
    }

    public List<String> print(Method method) {
        var annotations = annotationPrinter.print(method.getAnnotations());
        var visibility = visibility(method.getVisibility());
        var traits = method.getTraits().stream().map(Enum::name).map(String::toLowerCase).collect(joining(" "));

        var genericTypes = method.getGenericTypes();
        var generics = !genericTypes.isEmpty()
            ? genericTypes.stream().map(GenericType::toString).collect(joining(", ", "<", ">"))
            : "";

        var returnType = method.getReturnType().accept(typePrinter);
        var arguments = method
            .getArguments()
            .stream()
            .map(argument -> argument.getValue().accept(typePrinter) + " " + argument.getName())
            .collect(joining(", ", method.getName() + "(", ")"));
        var bodyContent = printBody(method.getBody());

        var header = join(" ", visibility, traits, generics, returnType, arguments);

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

    @SuppressWarnings("SameParameterValue")
    private String join(String delimiter, String... parts) {
        return Stream.of(parts).filter(headerPart -> !headerPart.isBlank()).collect(joining(delimiter));
    }

    private LinkedList<String> printBody(List<Expression> expressions) {
        if (expressions.isEmpty()) {
            return new LinkedList<>(List.of(";"));
        }
        var lines = expressions
            .stream()
            .map(Expression::asString)
            .collect(toCollection(LinkedList::new));
        lines.addFirst(" {");
        lines.addLast("}");

        return lines;
    }
}