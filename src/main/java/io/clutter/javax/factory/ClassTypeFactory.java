package io.clutter.javax.factory;

import io.clutter.filter.Filters;
import io.clutter.writer.model.classtype.ClassType;
import io.clutter.writer.model.classtype.modifiers.ClassModifiers;
import io.clutter.writer.model.classtype.modifiers.ClassTrait;
import io.clutter.writer.model.classtype.modifiers.ClassVisibility;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

final public class ClassTypeFactory {

    /**
     * Implements or extends given class with prefix
     */
    public static ClassType subclassWithPrefix(TypeElement typeElement, String prefix) {
        String qualifiedName = typeElement.getQualifiedName().toString();
        String packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf('.'));

        ClassType classType = new ClassType(packageName + prefix + typeElement.getSimpleName());

        if (Filters.CLASS.test(typeElement)) {
            classType.setParentClass(qualifiedName);
        } else if (Filters.INTERFACE.test(typeElement)) {
            classType.setInterfaces(qualifiedName);
        } else {
            throw new IllegalArgumentException("TypeElement is not element or interface");
        }

        return classType;
    }

    /**
     * Implements or extends given class with postfix
     */
    public static ClassType subclassWithPostfix(TypeElement typeElement, String postfix) {

        String qualifiedName = typeElement.getQualifiedName().toString();
        ClassType classType = new ClassType(typeElement.getQualifiedName() + postfix);

        if (Filters.CLASS.test(typeElement)) {
            classType.setParentClass(qualifiedName);
        } else if (Filters.INTERFACE.test(typeElement)) {
            classType.setInterfaces(qualifiedName);
        } else {
            throw new IllegalArgumentException("TypeElement is not element or interface");
        }

        return classType;
    }

    private static ClassModifiers modifiers(Set<Modifier> javaxModifiers) {
        final ClassVisibility visibility;
        if (javaxModifiers.contains(Modifier.PUBLIC)) {
            visibility = ClassVisibility.PUBLIC;
        } else if (javaxModifiers.contains(Modifier.PROTECTED)) {
            visibility = ClassVisibility.PROTECTED;
        } else if (javaxModifiers.contains(Modifier.PRIVATE)) {
            visibility = ClassVisibility.PRIVATE;
        } else {
            visibility = ClassVisibility.PACKAGE_PRIVATE;
        }
        Set<String> traitsValues = Stream.of(ClassTrait.values())
                .map(String::valueOf)
                .collect(toSet());

        ClassTrait[] traits = javaxModifiers
                .stream()
                .map(String::valueOf)
                .map(String::toLowerCase)
                .filter(traitsValues::contains)
                .map(ClassTrait::valueOf)
                .toArray(ClassTrait[]::new);

        return new ClassModifiers(visibility, traits);
    }

}
