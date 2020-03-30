package io.clutter.writer.printer;

import io.clutter.model.classtype.ClassType;
import io.clutter.writer.JavaFileBuilder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import static io.clutter.writer.printer.PrinterUtils.joinNonBlank;
import static io.clutter.writer.printer.PrinterUtils.nested;
import static java.util.Objects.requireNonNullElseGet;
import static java.util.stream.Collectors.toList;

final public class ClassTypeWriter {

    private static final String SEPARATOR = " ";

    private final TypePrinter typePrinter;
    private final AnnotationPrinter annotationPrinter;
    private final ConstructorPrinter constructorPrinter;
    private final MethodPrinter methodPrinter;
    private final FieldPrinter fieldPrinter;

    private ClassTypeWriter(
            TypePrinter typePrinter,
            AnnotationPrinter annotationPrinter,
            ConstructorPrinter constructorPrinter,
            FieldPrinter fieldPrinter,
            MethodPrinter methodPrinter) {
        this.typePrinter = typePrinter;
        this.annotationPrinter = annotationPrinter;
        this.constructorPrinter = constructorPrinter;
        this.fieldPrinter = fieldPrinter;
        this.methodPrinter = methodPrinter;
    }

    public JavaFileBuilder toJavaFileBuilder(ClassType classType) {
        List<String> lines = new LinkedList<>();
        lines.addAll(annotations(classType));
        lines.addAll(classDeclaration(classType));
        return new JavaFileBuilder(
                classType.getPackage(),
                classType.getFullyQualifiedName(),
                typePrinter.getImports(),
                lines
        );
    }

    private List<String> classDeclaration(ClassType classType) {
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
                .map(annotationPrinter::print)
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
                .map(fieldPrinter::print)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private List<List<String>> constructors(ClassType classType) {
        return classType
                .getConstructors()
                .stream()
                .map(constructorPrinter::print)
                .collect(toList());
    }

    private List<List<String>> methods(ClassType classType) {
        return classType
                .getMethods()
                .stream()
                .map(methodPrinter::print)
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

    public static class Builder {
        private TypePrinter typePrinter;
        private AnnotationPrinter annotationPrinter;
        private ConstructorPrinter constructorPrinter;
        private FieldPrinter fieldPrinter;
        private MethodPrinter methodPrinter;

        public Builder setTypePrinter(TypePrinter typePrinter) {
            this.typePrinter = typePrinter;
            return this;
        }

        public Builder setAnnotationPrinter(AnnotationPrinter annotationPrinter) {
            this.annotationPrinter = annotationPrinter;
            return this;
        }

        public Builder setFieldPrinter(FieldPrinter fieldPrinter) {
            this.fieldPrinter = fieldPrinter;
            return this;
        }

        public Builder setConstructorPrinter(ConstructorPrinter constructorPrinter) {
            this.constructorPrinter = constructorPrinter;
            return this;
        }

        public Builder setMethodPrinter(MethodPrinter methodPrinter) {
            this.methodPrinter = methodPrinter;
            return this;
        }

        public TypePrinter getTypePrinter() {
            return typePrinter;
        }

        public AnnotationPrinter getAnnotationPrinter() {
            return annotationPrinter;
        }

        public ConstructorPrinter getConstructorPrinter() {
            return constructorPrinter;
        }

        public FieldPrinter getFieldPrinter() {
            return fieldPrinter;
        }

        public MethodPrinter getMethodPrinter() {
            return methodPrinter;
        }

        public ClassTypeWriter build() {
            var typePrinter = orElse(this.typePrinter, AutoImportingTypePrinter::new);
            return new ClassTypeWriter(
                    typePrinter,
                    orElse(annotationPrinter, () -> new AnnotationPrinter(typePrinter)),
                    orElse(constructorPrinter, () -> new ConstructorPrinter(typePrinter)),
                    orElse(fieldPrinter, () -> new FieldPrinter(typePrinter)),
                    orElse(methodPrinter, () -> new MethodPrinter(typePrinter))
            );
        }

        private <T> T orElse(T type, Supplier<T> orElse) {
            return requireNonNullElseGet(type, orElse);
        }
    }
}