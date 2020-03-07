package io.clutter.javax.factory;

import io.clutter.javax.extractor.Filters;
import io.clutter.model.classtype.ClassType;

import javax.lang.model.element.TypeElement;

final public class ClassTypeFactory {

    /**
     * Implements or extends given class with prefix
     */
    public static ClassType extendWithPrefix(TypeElement typeElement, String prefix) {
        String qualifiedName = typeElement.getQualifiedName().toString();
        String packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf('.'));

        ClassType classType = new ClassType(packageName + '.' + prefix + typeElement.getSimpleName());

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
    public static ClassType extendWithPostfix(TypeElement typeElement, String postfix) {

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
}
