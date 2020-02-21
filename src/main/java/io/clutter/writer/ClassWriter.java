package io.clutter.writer;

import io.clutter.writer.annotation.AnnotationType;
import io.clutter.writer.classtype.ClassType;
import io.clutter.writer.constructor.Constructor;
import io.clutter.writer.field.Field;
import io.clutter.writer.method.Method;
import io.clutter.writer.param.Params;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

final public class ClassWriter {

    public static List<String> lines(ClassType classType) {
        String qualifiedName = classType.getFullQualifiedName();
        int classNameIndex = qualifiedName.lastIndexOf('.');
        String packageName = qualifiedName.substring(0, classNameIndex);
        String className = qualifiedName.substring(classNameIndex + 1);

        List<String> lines = new LinkedList<>();
        lines.add(format("package %s;", packageName));
        lines.add("");
        lines.addAll(annotations(classType.getAnnotations()));
        lines.add(format("%s class %s %s {", classType.getClassModifiers(), className, superClasses(classType)));
        lines.add("");
        lines.addAll(tabbed(fields(classType.getFields())));
        lines.addAll(tabbed(constructors(classType.getConstructors(), className)));
        lines.addAll(tabbed(methods(classType.getMethods())));
        lines.add("}");

        return lines;
    }

    private static String superClasses(ClassType classType) {
        return classType.getParentClass()
                .map(" extends "::concat)
                .orElse("")
                .concat(
                        classType.getInterfaces().isEmpty()
                                ? "" : " implements "
                                .concat(classType.getInterfaces()
                                        .stream()
                                        .map(String::valueOf)
                                        .collect(joining(", "))));
    }

    private static List<String> constructors(Set<Constructor> constructors, String className) {
        List<String> lines = new LinkedList<>();
        constructors.forEach(constructor -> {
            lines.addAll(annotations(constructor.getAnnotations()));
            lines.add(format("%s %s(%s) {", constructor.getVisibility(), className, params(constructor.getParams())));
            lines.addAll(tabbed(constructor.getBody()));
            lines.add("}");
            lines.add("");
        });
        return lines;
    }

    private static List<String> fields(Set<Field> fields) {
        List<String> lines = new LinkedList<>();
        fields.forEach(field -> {
            lines.addAll(annotations(field.getAnnotations()));
            lines.add(format("%s %s %s;", field.getModifiers(), field.getType(), field.getName()));
            lines.add("");
        });
        return lines;
    }

    private static List<String> methods(Set<Method> methods) {
        List<String> lines = new LinkedList<>();
        methods.forEach(method -> {
            lines.addAll(annotations(method.getAnnotations()));
            lines.add(format("%s %s %s(%s) {", method.getModifiers(), method.getReturnType(), method.getName(), params(method.getParams())));
            lines.addAll(tabbed(method.getBody()));
            lines.add("}");
            lines.add("");
        });
        return lines;
    }

    private static String params(Params params) {
        return params
                .entrySet()
                .stream()
                .map(param -> format("%s %s", param.getValue(), param.getKey())) // order is: arg_type arg_name
                .collect(joining(", "));
    }

    private static List<String> tabbed(List<String> lines) {
        return lines.stream().map("\t"::concat).collect(toList());
    }

    private static List<String> annotations(List<AnnotationType> annotations) {
        return annotations.stream()
                .map(AnnotationType::toString)
                .collect(Collectors.toList());
    }
}
