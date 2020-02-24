package io.clutter.writer;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.classtype.ClassType;
import io.clutter.writer.model.classtype.InterfaceType;
import io.clutter.writer.model.constructor.Constructor;
import io.clutter.writer.model.field.Field;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.method.modifiers.MethodTrait;
import io.clutter.writer.model.param.Param;

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
        String traits = classType.getTraits().stream().map(String::valueOf).collect(joining(" "));

        List<String> lines = new LinkedList<>();
        lines.add(format("package %s;", packageName));
        lines.add("");
        lines.addAll(annotations(classType.getAnnotations()));
        lines.add(format("%s %s class %s %s {", classType.getVisibility(), traits, className, classType.getParentClass().map(" extends "::concat).orElse("") + extendedInterfaces(classType.getInterfaces())));
        lines.add("");
        lines.addAll(tabbed(fields(classType.getFields())));
        lines.addAll(tabbed(constructors(classType.getConstructors(), className)));
        lines.addAll(tabbed(methods(classType.getMethods())));
        lines.add("}");

        return lines;
    }

    public static List<String> lines(InterfaceType interfaceType) {
        String qualifiedName = interfaceType.getFullQualifiedName();
        int classNameIndex = qualifiedName.lastIndexOf('.');
        String packageName = qualifiedName.substring(0, classNameIndex);
        String className = qualifiedName.substring(classNameIndex + 1);

        List<String> lines = new LinkedList<>();
        lines.add(format("package %s;", packageName));
        lines.add("");
        lines.addAll(annotations(interfaceType.getAnnotations()));
        lines.add(format("%s interface %s %s {", interfaceType.getVisibility(), className, extendedInterfaces(interfaceType.getInterfaces())));
        lines.add("");
        lines.addAll(tabbed(methods(interfaceType.getMethods())));
        lines.add("}");

        return lines;
    }

    private static String extendedInterfaces(Set<String> interfaces) {
        if (interfaces.isEmpty()) {
            return "";
        }
        return " implements " + interfaces
                .stream()
                .map(String::valueOf)
                .collect(joining(", "));
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
            lines.add(format("%s %s %s;", field.getVisibility(), field.getType(), field.getName()));
            lines.add("");
        });
        return lines;
    }

    private static List<String> methods(Set<Method> methods) {
        List<String> lines = new LinkedList<>();
        methods.forEach(method -> {
            String traits = method.getTraits().stream().map(String::valueOf).collect(joining(" "));
            if (method.getTraits().contains(MethodTrait.ABSTRACT)) {
                lines.add(format("%s %s %s %s(%s);", method.getVisibility(), traits, method.getReturnType(), method.getName(), params(method.getParams())));
            } else {
                lines.addAll(annotations(method.getAnnotations()));
                lines.add(format("%s %s %s %s(%s) {", method.getVisibility(), traits, method.getReturnType(), method.getName(), params(method.getParams())));
                lines.addAll(tabbed(method.getBody()));
                lines.add("}");
                lines.add("");
            }
        });
        return lines;
    }

    private static String params(Set<Param> params) {
        return params
                .stream()
                .map(param -> format("%s %s", param.getValue(), param.getName())) // order is: arg_type arg_name
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
