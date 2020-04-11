package io.clutter.printer;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.field.Field;
import io.clutter.model.method.Method;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static io.clutter.printer.PrinterUtils.joinNonBlank;
import static io.clutter.printer.PrinterUtils.nested;
import static java.util.stream.Collectors.toList;

public class ClassPrinter {

    private static final String SEPARATOR = " ";

    private final TypePrinter typePrinter;
    private final AnnotationPrinter annotationPrinter;
    private final ConstructorPrinter constructorPrinter;
    private final MethodPrinter methodPrinter;
    private final FieldPrinter fieldPrinter;

    public ClassPrinter(TypePrinter typePrinter) {
        this.typePrinter = typePrinter;
        this.annotationPrinter = new AnnotationPrinter(this.typePrinter);
        this.constructorPrinter = new ConstructorPrinter(this.typePrinter);
        this.methodPrinter = new MethodPrinter(this.typePrinter);
        this.fieldPrinter = new FieldPrinter(this.typePrinter);
    }

    public List<String> print(ClassType classType) {
        List<String> lines = new LinkedList<>();
        lines.addAll(annotations(classType));
        lines.addAll(classBody(classType));
        return lines;
    }

    protected List<String> printAnnotation(AnnotationType annotationType) {
        return annotationPrinter.print(annotationType);
    }

    protected List<String> printField(Field field) {
        return fieldPrinter.print(field);
    }

    protected List<String> printConstructor(Constructor constructor) {
        return constructorPrinter.print(constructor);
    }

    protected List<String> printMethod(Method method) {
        return methodPrinter.print(method);
    }

    private List<String> classBody(ClassType classType) {
        List<String> lines = new LinkedList<>();
        lines.add(joinNonBlank(List.of(
                visibility(classType),
                traits(classType),
                "class",
                className(classType),
                superClass(classType),
                interfaces(classType),
                "{"),
                SEPARATOR
        ));
        lines.add("");
        lines.addAll(body(classType));
        lines.add("}");
        return lines;
    }

    private List<String> annotations(ClassType classType) {
        return classType.getAnnotations()
                .stream()
                .map(this::printAnnotation)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private List<String> body(ClassType classType) {
        return nested((lines) -> {
            lines.addAll(fields(classType));
            constructors(classType).forEach(constructor -> {
                lines.add("");
                lines.addAll(constructor);
            });
            methods(classType).forEach(method -> {
                lines.add("");
                lines.addAll(method);
            });
        });
    }

    private List<String> fields(ClassType classType) {
        return classType
                .getFields()
                .stream()
                .map(this::printField)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private List<List<String>> constructors(ClassType classType) {
        return classType
                .getConstructors()
                .stream()
                .map(this::printConstructor)
                .collect(toList());
    }

    private List<List<String>> methods(ClassType classType) {
        return classType
                .getMethods()
                .stream()
                .map(this::printMethod)
                .collect(toList());
    }

    private String visibility(ClassType classType) {
        return String.valueOf(classType.getVisibility());
    }

    private String traits(ClassType classType) {
        return joinNonBlank(classType.getTraits(), " ");
    }

    private String className(ClassType classType) {
        return classType.getSimpleName() + genericParams(classType);
    }

    private String genericParams(ClassType classType) {
        return typePrinter.printGenerics(classType.getGenericParameters());
    }

    private String superClass(ClassType classType) {
        return classType
                .getSuperclass()
                .map(typePrinter::print)
                .map(" extends "::concat)
                .orElse("");
    }

    private String interfaces(ClassType classType) {
        return classType
                .getInterfaces()
                .stream()
                .map(typePrinter::print)
                .reduce((first, second) -> first + ", " + second)
                .map(" implements "::concat)
                .orElse("");
    }
}