package io.clutter.javax.factory;

import io.clutter.javax.extractor.Filters;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.PrimitiveType;
import io.clutter.model.type.Type;

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
            classType.setParentClass(getBoxedType(typeElement));
        } else if (Filters.INTERFACE.test(typeElement)) {
            classType.setInterfaces(getBoxedType(typeElement));
        } else {
            throw new IllegalArgumentException("TypeElement is not element or interface");
        }

        return classType;
    }

    /**
     * Implements or extends given class with postfix
     */
    public static ClassType extendWithPostfix(TypeElement typeElement, String postfix) {

        ClassType classType = new ClassType(typeElement.getQualifiedName() + postfix);

        if (Filters.CLASS.test(typeElement)) {
            classType.setParentClass(getBoxedType(typeElement));
        } else if (Filters.INTERFACE.test(typeElement)) {
            classType.setInterfaces(getBoxedType(typeElement));
        } else {
            throw new IllegalArgumentException("TypeElement is not element or interface");
        }

        return classType;
    }

    private static BoxedType getBoxedType(TypeElement typeElement) {
        Type type = TypeFactory.of(typeElement.asType());
        if (type instanceof PrimitiveType) {
            throw new IllegalArgumentException("Type element is primitive");
        }
        return (BoxedType) type;
    }
}
