package io.clutter.writer;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.classtype.InterfaceType;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.field.Field;
import io.clutter.model.method.Method;
import io.clutter.model.method.modifiers.MethodTrait;
import io.clutter.model.param.Param;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.Type;
import io.clutter.processor.JavaFile;

import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.*;

@Deprecated
final public class JavaFileGenerator {

    private TypePrinter typePrinter;

    public JavaFile generate(ClassType classType) {
        return new JavaFile() {

            @Override
            public String getFullQualifiedName() {
                return classType.getFullyQualifiedName();
            }

            @Override
            public List<String> getLines() {
                return lines(classType);
            }
        };
    }

    public JavaFile generate(InterfaceType interfaceType) {
        return new JavaFile() {

            @Override
            public String getFullQualifiedName() {
                return interfaceType.getFullyQualifiedName();
            }

            @Override
            public List<String> getLines() {
                return lines(interfaceType);
            }
        };
    }

    public List<String> lines(ClassType classType) {
        String qualifiedName = classType.getFullyQualifiedName();
        int classNameIndex = qualifiedName.lastIndexOf('.');
        String packageName = qualifiedName.substring(0, classNameIndex);
        String className = qualifiedName.substring(classNameIndex + 1);

        Set<String> imports = imports(classType);
        typePrinter = new TypePrinter();

        List<String> lines = new LinkedList<>();
        lines.add(format("package %s;", packageName));
        lines.add("");
        if (!imports.isEmpty()) {
            imports.stream()
                    .map(import_ -> String.format("import %s;", import_))
                    .forEach(lines::add);
            lines.add("");
        }
        lines.addAll(annotations(classType.getAnnotations()));
        lines.add(trimFormat("%s %s class %s%s %s %s {",
                classType.getVisibility(),
                join(classType.getTraits()),
                className,
                typeToString(classType.getWildcardTypes()),
                extendedClass(classType.getParentClass()),
                implementedInterfaces(classType.getInterfaces()))
        );
        lines.addAll(tabbed(fields(classType.getFields())));
        lines.addAll(tabbed(constructors(classType.getConstructors(), className)));
        lines.addAll(tabbed(methods(classType.getMethods())));
        lines.add("}");

        return lines;
    }

    @SuppressWarnings("all")
    private String extendedClass(Optional<BoxedType> parentClass) {
        return parentClass.map(this::typeToString).map(" extends "::concat).orElse("");
    }

    public List<String> lines(InterfaceType interfaceType) {
        String qualifiedName = interfaceType.getFullyQualifiedName();
        int classNameIndex = qualifiedName.lastIndexOf('.');
        String packageName = qualifiedName.substring(0, classNameIndex);
        String className = qualifiedName.substring(classNameIndex + 1);

        Set<String> imports = imports(interfaceType);
        typePrinter = new TypePrinter();

        List<String> lines = new LinkedList<>();
        lines.add(format("package %s;", packageName));
        lines.add("");
        lines.addAll(annotations(interfaceType.getAnnotations()));
        lines.add(trimFormat("public interface %s%s %s {",
                className,
                typeToString(interfaceType.getWildcardTypes()),
                extendedInterfaces(interfaceType.getInterfaces()))
                .strip()
        );
        lines.addAll(tabbed(interfaceMethods(interfaceType.getMethods())));
        lines.add("}");

        return lines;
    }

    private String implementedInterfaces(Set<BoxedType> interfaces) {
        return interfaces
                .stream()
                .map(this::typeToString)
                .reduce((first, second) -> first + ", " + second)
                .map(" implements "::concat)
                .orElse("");
    }

    private String extendedInterfaces(Set<BoxedType> interfaces) {
        return interfaces
                .stream()
                .map(this::typeToString)
                .reduce((first, second) -> first + ", " + second)
                .map(" extends "::concat)
                .orElse("");
    }

    private List<String> constructors(Collection<Constructor> constructors, String className) {
        List<String> lines = new LinkedList<>();
        constructors.forEach(constructor -> {
            lines.add("");
            lines.addAll(annotations(constructor.getAnnotations()));
            lines.add(trimFormat("%s %s %s(%s) {",
                    constructor.getVisibility(),
                    typeToString(constructor.getWildcardTypes()),
                    className,
                    params(constructor.getParams()))
            );
            lines.addAll(tabbed(constructor.getBody()));
            lines.add("}");
        });
        return lines;
    }

    private List<String> fields(Collection<Field> fields) {
        List<String> lines = new LinkedList<>();
        lines.add("");
        fields.forEach(field -> {
            lines.addAll(annotations(field.getAnnotations()));
            lines.add(trimFormat("%s %s %s %s%s;",
                    field.getVisibility(),
                    join(field.getTraits()),
                    typeToString(field.getType()),
                    field.getName(),
                    field.getValue().map(" = "::concat).orElse(""))
            );
        });
        return lines;
    }

    private List<String> methods(Collection<Method> methods) {
        List<String> lines = new LinkedList<>();
        methods.forEach(method -> {
            lines.add("");
            lines.addAll(annotations(method.getAnnotations()));
            lines.add(trimFormat("%s %s %s %s %s(%s);",
                    method.getVisibility(),
                    join(method.getTraits()),
                    typeToString(method.getWildcardTypes()),
                    typeToString(method.getReturnType()),
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

    private List<String> interfaceMethods(Collection<Method> methods) {
        List<String> lines = new LinkedList<>();
        methods.forEach(method -> {
            lines.add("");
            lines.addAll(annotations(method.getAnnotations()));
            lines.add(trimFormat("%s %s %s %s(%s);",
                    join(method.getTraits()),
                    typeToString(method.getWildcardTypes()),
                    method.getReturnType(),
                    method.getName(),
                    params(method.getParams()))
                    .strip()
            );
        });
        return lines;
    }

    private String params(Collection<Param> params) {
        return params
                .stream()
                .map(param -> typeToString(param.getValue()) + " " + param.getName())
                .collect(joining(", "));
    }

    private List<String> annotations(Collection<AnnotationType> annotations) {
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

    private Set<String> imports(InterfaceType interfaceType) {
        List<Type> types = new LinkedList<>(interfaceType.getWildcardTypes());
        interfaceType
                .getMethods()
                .stream()
                .map(Method::getReturnType)
                .forEach(types::add);
        interfaceType
                .getMethods()
                .stream()
                .map(Method::getParams)
                .flatMap(Collection::stream)
                .map(Param::getValue)
                .forEach(types::add);
        interfaceType
                .getMethods()
                .stream()
                .map(Method::getWildcardTypes)
                .flatMap(Collection::stream)
                .forEach(types::add);

        return types.stream()
                .map(Type::getType)
                .map(aClass -> {
                    Class<?> a = aClass;
                    while (a.isArray()) {
                        a = a.getComponentType();
                    }
                    return a;
                })
                .filter(not(Class::isPrimitive))
                .map(Class::getCanonicalName)
                .collect(toSet());
    }

    private Set<String> imports(ClassType classType) {
        List<Type> types = new LinkedList<>(classType.getWildcardTypes());
        classType.getConstructors()
                .stream()
                .map(Constructor::getParams)
                .flatMap(Collection::stream)
                .map(Param::getValue)
                .forEach(types::add);
        classType.getConstructors()
                .stream()
                .map(Constructor::getWildcardTypes)
                .flatMap(Collection::stream)
                .forEach(types::add);
        classType
                .getFields()
                .stream()
                .map(Field::getType)
                .forEach(types::add);
        classType
                .getMethods()
                .stream()
                .map(Method::getReturnType)
                .forEach(types::add);
        classType
                .getMethods()
                .stream()
                .map(Method::getParams)
                .flatMap(Collection::stream)
                .map(Param::getValue)
                .forEach(types::add);
        classType
                .getMethods()
                .stream()
                .map(Method::getWildcardTypes)
                .flatMap(Collection::stream)
                .forEach(types::add);

        return types.stream()
                .map(Type::getType)
                .map(aClass -> {
                    Class<?> a = aClass;
                    while (a.isArray()) {
                        a = a.getComponentType();
                    }
                    return a;
                })
                .filter(not(Class::isPrimitive))
                .map(Class::getCanonicalName)
                .collect(toSet());
    }

    private String typeToString(Collection<? extends Type> type) {
        if (type.isEmpty()) return "";
        return type.stream().map(typePrinter::print).collect(Collectors.joining(", ", "<", ">"));
    }

    private String typeToString(Type type) {
        return typePrinter.print(type);
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

    private void replaceLast(List<String> lines, String pattern, String replacement) {
        lines.add(lines.remove(lines.size() - 1).replace(pattern, replacement));
    }

    private String trimFormat(String input, Object... params) {
        return format(input, params).replaceAll("\\s+", " ");
    }
}
