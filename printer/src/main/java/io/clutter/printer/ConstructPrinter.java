package io.clutter.printer;

import io.clutter.model.clazz.Construct;
import io.clutter.model.common.Visibility;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.GenericType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

final public class ConstructPrinter {

    private final TypePrinter typePrinter;
    private final AnnotationPrinter annotationPrinter;
    private final FieldPrinter fieldPrinter;
    private final ConstructorPrinter constructorPrinter;
    private final MethodPrinter methodPrinter;

    public ConstructPrinter() {
        var packagePrinter = new PackagePrinter();
        this.typePrinter = new TypePrinter(packagePrinter);
        this.annotationPrinter = new AnnotationPrinter();
        this.fieldPrinter = new FieldPrinter();
        this.constructorPrinter = new ConstructorPrinter();
        this.methodPrinter = new MethodPrinter();
    }

    public List<String> print(Construct construct) {
        var annotations = annotationPrinter.print(construct.getAnnotations());
        var visibility = visibility(construct.getVisibility());
        var traits = traits(construct);
        var constructType = construct.getConstructType().name().toLowerCase();

        var genericTypes = construct.getGenericTypes();
        var generics = generics(genericTypes);

        var parentClass = parentClass(construct.getParentClass());
        var interfaces = interfaces(construct.getInterfaces());

        var body = new ArrayList<>(annotations);
        var header = join(" ", visibility, traits, constructType, construct.getName(), generics, parentClass, interfaces);

        body.add(header + "{");
        body.addAll(print(construct.getFields(), fieldPrinter::print));
        body.add("");
        body.addAll(print(
            construct.getConstructors(),
            constructor -> constructorPrinter.print(constructor, construct.getName())
        ));
        body.add("");
        body.addAll(print(construct.getMethods(), methodPrinter::print));
        body.add("}");

        return body;
    }

    private String visibility(Visibility visibility) {
        if (visibility != null) {
            return visibility.name().toLowerCase();
        }
        return "";
    }

    private String traits(Construct construct) {
        return construct.getTraits().stream().map(Enum::name).map(String::toLowerCase).collect(joining(" "));
    }

    private String generics(List<GenericType> genericTypes) {
        return !genericTypes.isEmpty()
            ? genericTypes.stream().map(GenericType::toString).collect(joining(", ", "<", ">"))
            : "";
    }

    private String parentClass(BoxedType parentClass) {
        return parentClass != null
            ? "extends " + parentClass.accept(typePrinter)
            : "";
    }

    private String interfaces(List<BoxedType> interfaces) {
        return !interfaces.isEmpty()
            ? "implements " + interfaces.stream().map(s -> s.accept(typePrinter)).collect(joining(", "))
            : "";
    }

    @SuppressWarnings("SameParameterValue")
    private String join(String delimiter, String... parts) {
        return Stream.of(parts)
            .filter(part -> !part.isBlank())
            .collect(joining(delimiter));
    }

    private <T> List<String> print(List<T> elements, Function<T, List<String>> printer) {
        return elements
            .stream()
            .map(printer)
            .flatMap(Collection::stream)
            .collect(toList());
    }
}