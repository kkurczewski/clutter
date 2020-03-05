package io.clutter.writer;

import io.clutter.processor.JavaFile;
import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.classtype.ClassType;
import io.clutter.writer.model.classtype.InterfaceType;
import io.clutter.writer.model.constructor.Constructor;
import io.clutter.writer.model.field.Field;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.method.modifiers.MethodTrait;
import io.clutter.writer.model.param.Param;
import io.clutter.writer.model.type.WildcardType;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

final public class JavaFileGenerator {

    public static JavaFile generate(ClassType classType) {
        return new JavaFile() {

            @Override
            public String getFullQualifiedName() {
                return classType.getFullyQualifiedName();
            }

            @Override
            public void writeTo(Writer writer) {
                lines(classType).forEach(line -> {
                    try {
                        writer.write(line);
                        writer.write(System.lineSeparator());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        };
    }

    public static JavaFile generate(InterfaceType interfaceType) {
        return new JavaFile() {

            @Override
            public String getFullQualifiedName() {
                return interfaceType.getFullyQualifiedName();
            }

            @Override
            public void writeTo(Writer writer) {
                lines(interfaceType).forEach(line -> {
                    try {
                        writer.write(line);
                        writer.write(System.lineSeparator());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        };
    }

    public static List<String> lines(ClassType classType) {
        String qualifiedName = classType.getFullyQualifiedName();
        int classNameIndex = qualifiedName.lastIndexOf('.');
        String packageName = qualifiedName.substring(0, classNameIndex);
        String className = qualifiedName.substring(classNameIndex + 1);

        List<String> lines = new LinkedList<>();
        lines.add(format("package %s;", packageName));
        lines.add("");
        lines.addAll(annotations(classType.getAnnotations()));
        lines.add(trimFormat("%s %s class %s%s %s %s {",
                classType.getVisibility(),
                join(classType.getTraits()),
                className,
                generics(classType.getGenericTypes()),
                classType.getParentClass().map(" extends "::concat).orElse(""),
                implementedInterfaces(classType.getInterfaces()))
        );
        lines.addAll(tabbed(fields(classType.getFields())));
        lines.addAll(tabbed(constructors(classType.getConstructors(), className)));
        lines.addAll(tabbed(methods(classType.getMethods())));
        lines.add("}");

        return lines;
    }

    public static List<String> lines(InterfaceType interfaceType) {
        String qualifiedName = interfaceType.getFullyQualifiedName();
        int classNameIndex = qualifiedName.lastIndexOf('.');
        String packageName = qualifiedName.substring(0, classNameIndex);
        String className = qualifiedName.substring(classNameIndex + 1);

        List<String> lines = new LinkedList<>();
        lines.add(format("package %s;", packageName));
        lines.add("");
        lines.addAll(annotations(interfaceType.getAnnotations()));
        lines.add(trimFormat("public interface %s%s %s {",
                className,
                generics(interfaceType.getGenericTypes()),
                extendedInterfaces(interfaceType.getInterfaces()))
                .strip()
        );
        lines.addAll(tabbed(interfaceMethods(interfaceType.getMethods())));
        lines.add("}");

        return lines;
    }

    private static String implementedInterfaces(Collection<String> interfaces) {
        return interfaces
                .stream()
                .map(String::valueOf)
                .reduce((first, second) -> first + ", " + second)
                .map(" implements "::concat)
                .orElse("");
    }

    private static String extendedInterfaces(Collection<String> interfaces) {
        return interfaces
                .stream()
                .map(String::valueOf)
                .reduce((first, second) -> first + ", " + second)
                .map(" extends "::concat)
                .orElse("");
    }

    private static List<String> constructors(Collection<Constructor> constructors, String className) {
        List<String> lines = new LinkedList<>();
        constructors.forEach(constructor -> {
            lines.add("");
            lines.addAll(annotations(constructor.getAnnotations()));
            lines.add(trimFormat("%s %s %s(%s) {",
                    constructor.getVisibility(),
                    generics(constructor.getGenericTypes()),
                    className,
                    params(constructor.getParams()))
            );
            lines.addAll(tabbed(constructor.getBody()));
            lines.add("}");
        });
        return lines;
    }

    private static List<String> fields(Collection<Field> fields) {
        List<String> lines = new LinkedList<>();
        lines.add("");
        fields.forEach(field -> {
            lines.addAll(annotations(field.getAnnotations()));
            lines.add(trimFormat("%s %s %s %s%s;",
                    field.getVisibility(),
                    join(field.getTraits()),
                    field.getType(),
                    field.getName(),
                    field.getValue().map(" = "::concat).orElse(""))
            );
        });
        return lines;
    }

    private static List<String> methods(Collection<Method> methods) {
        List<String> lines = new LinkedList<>();
        methods.forEach(method -> {
            lines.add("");
            lines.addAll(annotations(method.getAnnotations()));
            lines.add(trimFormat("%s %s %s %s %s(%s);",
                    method.getVisibility(),
                    join(method.getTraits()),
                    generics(method.getGenericTypes()),
                    method.getReturnType(),
                    method.getName(),
                    params(method.getParams()))
                    .strip()
            );
            if (!method.getTraits().contains(MethodTrait.ABSTRACT)) {
                replaceLast(lines, ";", " {");
                lines.addAll(tabbed(method.getBody()));
                lines.add("}");
            }
        });
        return lines;
    }

    private static List<String> interfaceMethods(Collection<Method> methods) {
        List<String> lines = new LinkedList<>();
        methods.forEach(method -> {
            lines.add("");
            lines.addAll(annotations(method.getAnnotations()));
            lines.add(trimFormat("%s %s %s %s(%s);",
                    join(method.getTraits()),
                    generics(method.getGenericTypes()),
                    method.getReturnType(),
                    method.getName(),
                    params(method.getParams()))
                    .strip()
            );
        });
        return lines;
    }

    private static String params(Collection<Param> params) {
        return params
                .stream()
                .map(param -> param.getValue() + " " + param.getName())
                .collect(joining(", "));
    }

    private static List<String> annotations(Collection<AnnotationType> annotations) {
        return annotations.stream()
                .map(annotationType -> "@" + annotationType.getType() + annotationType
                        .getParams()
                        .stream()
                        .map(param -> param.getKey() + " = " + param.getValue())
                        .reduce((first, second) -> first + ", " + second)
                        .map(params -> "(" + params + ")")
                        .orElse(""))
                .collect(toList());
    }

    private static String generics(Set<WildcardType> genericType) {
        return genericType.stream()
                .map(String::valueOf)
                .reduce((first, second) -> first + ", " + second)
                .map(type -> "<" + type + ">")
                .orElse("");
    }

    private static <T> String join(Collection<T> traits) {
        return traits.stream()
                .map(String::valueOf)
                .reduce((first, second) -> first + " " + second)
                .orElse("");
    }

    private static List<String> tabbed(Collection<String> lines) {
        return lines.stream().map("\t"::concat).collect(toList());
    }

    private static void replaceLast(List<String> lines, String pattern, String replacement) {
        lines.add(lines.remove(lines.size() - 1).replace(pattern, replacement));
    }

    private static String trimFormat(String input, Object... params) {
        return format(input, params).replaceAll("\\s+", " ");
    }
}
