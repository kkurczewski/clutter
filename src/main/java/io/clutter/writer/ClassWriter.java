package io.clutter.writer;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.field.Field;
import io.clutter.model.method.Method;
import io.clutter.model.method.modifiers.MethodTrait;
import io.clutter.model.param.Param;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.Type;
import io.clutter.model.type.WildcardType;
import io.clutter.processor.JavaFile;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static io.clutter.writer.TypePrinter.DEFAULT_IMPORT;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

final public class ClassWriter {

    private final ClassType classType;
    private final String qualifiedName;
    private final String className;
    private final Headers headers;

    private final List<String> constructorLines = new LinkedList<>();
    private final List<String> fieldLines = new LinkedList<>();
    private final List<String> methodLines = new LinkedList<>();
    private final TypePrinter typePrinter = new TypePrinter();

    public ClassWriter(ClassType classType, Headers headers) {
        this.classType = classType;
        this.qualifiedName = classType.getFullyQualifiedName();
        this.className = qualifiedName.substring(classType.getPackage().length() + 1);
        this.headers = headers;
    }

    public ClassWriter(ClassType classType) {
        this(classType, Headers.ofPackage(classType.getPackage()));
    }

    public JavaFile generate() {
        List<String> body = new LinkedList<>(annotations(classType.getAnnotations()));
        body.add(trimFormat("%s %s class %s%s %s %s {",
                classType.getVisibility(),
                join(classType.getTraits()),
                className,
                generics(classType.getGenericParameters()),
                classType.getParentClass()
                        .map(this::importType)
                        .map(" extends "::concat)
                        .orElse(""),
                implementedInterfaces(classType.getInterfaces()))
        );
        body.addAll(tabbed(generateFields(classType.getFields())));
        body.addAll(tabbed(generateConstructors(classType.getConstructors(), className)));
        body.addAll(tabbed(generateMethods(classType.getMethods())));
        body.add("}");

        List<String> file = new LinkedList<>();
        file.addAll(headers.asLines());
        file.addAll(body);

        return javaFile(qualifiedName, file);
    }

    private JavaFile javaFile(String fullyQualifiedName, List<String> lines) {
        return new JavaFile() {

            @Override
            public String getFullQualifiedName() {
                return fullyQualifiedName;
            }

            @Override
            public List<String> getLines() {
                return lines;
            }
        };
    }

    private String implementedInterfaces(Set<BoxedType> interfaces) {
        return interfaces
                .stream()
                .map(this::importType)
                .reduce((first, second) -> first + ", " + second)
                .map(" implements "::concat)
                .orElse("");
    }

    private List<String> generateConstructors(Collection<Constructor> constructors, String className) {
        constructors.forEach(constructor -> {
            constructorLines.add("");
            constructorLines.addAll(annotations(constructor.getAnnotations()));
            constructorLines.add(trimFormat("%s %s %s(%s) {",
                    constructor.getVisibility(),
                    generics(constructor.getWildcardTypes()),
                    className,
                    params(constructor.getParams()))
            );
            constructorLines.addAll(tabbed(constructor.getBody()));
            constructorLines.add("}");
        });
        return constructorLines;
    }

    private List<String> generateFields(Collection<Field> fields) {
        fieldLines.add("");
        fields.forEach(field -> {
            fieldLines.addAll(annotations(field.getAnnotations()));
            fieldLines.add(trimFormat("%s %s %s %s%s;",
                    field.getVisibility(),
                    join(field.getTraits()),
                    importType(field.getType()),
                    field.getName(),
                    field.getValue().map(" = "::concat).orElse(""))
            );
        });
        return fieldLines;
    }

    private List<String> generateMethods(Collection<Method> methods) {
        methods.forEach(method -> {
            methodLines.add("");
            methodLines.addAll(annotations(method.getAnnotations()));
            methodLines.add(trimFormat("%s %s %s %s %s(%s);",
                    method.getVisibility(),
                    join(method.getTraits()),
                    generics(method.getWildcardTypes()),
                    importType(method.getReturnType()),
                    method.getName(),
                    params(method.getParams()))
            );
            if (!method.getTraits().contains(MethodTrait.ABSTRACT)) {
                replaceLast(methodLines, ";", " {");
                methodLines.addAll(tabbed(method.getBody()));
                methodLines.add("}");
            }
        });
        return methodLines;
    }

    private String params(Collection<Param> params) {
        return params
                .stream()
                .map(param -> importType(param.getValue()) + " " + param.getName())
                .collect(joining(", "));
    }

    private List<String> annotations(Collection<AnnotationType> annotations) {
        return annotations.stream()
                .map(annotationType -> "@" + annotationType.getType() + annotationType
                        .getParams()
                        .entrySet()
                        .stream()
                        .map(param -> param.getKey() + " = " + param.getValue())
                        .reduce((first, second) -> first + ", " + second)
                        .map(params -> "(" + params + ")")
                        .orElse(""))
                .collect(toList());
    }

    private String generics(Set<WildcardType> wildcardType) {
        return wildcardType.stream()
                .map(this::importType)
                .reduce((first, second) -> first + ", " + second)
                .map(type -> "<" + type + ">")
                .orElse("");
    }

    private <T> String join(Collection<T> traits) {
        return traits.stream()
                .map(String::valueOf)
                .reduce((first, second) -> first + " " + second)
                .orElse("");
    }

    private List<String> tabbed(Collection<String> lines) {
        return lines.stream().map("\t"::concat).collect(toList());
    }

    private String importType(Type type) {
        Class<?> classType = type.getType();
        while (classType.isArray()) {
            classType = classType.getComponentType();
        }
        if (!classType.isPrimitive() && !classType.getPackageName().equals(DEFAULT_IMPORT)) {
            headers.addImport(classType);
        }
        return typePrinter.print(type);
    }

    private void replaceLast(List<String> lines, String pattern, String replacement) {
        lines.add(lines.remove(lines.size() - 1).replace(pattern, replacement));
    }

    private String trimFormat(String input, Object... params) {
        return format(input, params).replaceAll("\\s+", " ");
    }
}
