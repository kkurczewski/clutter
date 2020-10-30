package io.clutter.javax.factory;

import io.clutter.model.clazz.Construct;
import io.clutter.model.ctor.Constructor;
import io.clutter.model.field.Field;
import io.clutter.model.method.Method;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.GenericType;

import javax.lang.model.element.*;
import java.util.List;

import static io.clutter.model.clazz.Construct.ConstructType;
import static java.util.stream.Collectors.toList;

final public class JxClassAdapter {

    public static Construct from(TypeElement classType) {
        var type = JxBoxedTypeAdapter.from(classType.getSuperclass());
        return new Construct()
            .setConstructType(construct(classType.getKind()))
            .setPackageName(classType.getEnclosingElement().toString())
            .setName(classType.getSimpleName().toString())
            .setAnnotations(annotations -> annotations.addAll(JxAnnotationAdapter.from(classType)))
            .setGenericTypes(generics -> generics.addAll(generics(classType)))
            .setVisibility(JxVisibilityAdapter.from(classType.getModifiers()))
            .setTraits(traits -> traits.addAll(JxTraitAdapter.from(classType.getModifiers())))
            .setInterfaces(interfaces -> interfaces.addAll(interfaces(classType)))
            .setParentClass(type.getType() != Object.class ? type : null)
            .setFields(fields -> fields.addAll(fields(classType)))
            .setConstructors(ctors -> ctors.addAll(constructors(classType)))
            .setMethods(methods -> methods.addAll(methods(classType)));
    }

    private static ConstructType construct(ElementKind kind) {
        switch (kind) {
            case INTERFACE:
                return ConstructType.INTERFACE;
            case CLASS:
                return ConstructType.CLASS;
            default:
                return null;
        }
    }

    private static List<Method> methods(TypeElement executableElements) {
        return executableElements
            .getEnclosedElements()
            .stream()
            .filter(element -> element.getKind() == ElementKind.METHOD)
            .map(ExecutableElement.class::cast)
            .map(JxMethodAdapter::from)
            .collect(toList());
    }

    private static List<Constructor> constructors(TypeElement classType) {
        return classType
            .getEnclosedElements()
            .stream()
            .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR)
            .map(ExecutableElement.class::cast)
            .map(JxConstructorAdapter::from)
            .collect(toList());
    }

    private static List<Field> fields(TypeElement classType) {
        return classType
            .getEnclosedElements()
            .stream()
            .filter(element -> element.getKind() == ElementKind.FIELD)
            .map(VariableElement.class::cast)
            .map(JxFieldAdapter::from)
            .collect(toList());
    }

    private static List<BoxedType> interfaces(TypeElement typeElement) {
        return typeElement.getInterfaces()
            .stream()
            .map(JxBoxedTypeAdapter::from)
            .collect(toList());
    }

    private static List<GenericType> generics(TypeElement typeElement) {
        return typeElement.getTypeParameters()
            .stream()
            .map(Element::asType)
            .map(JxGenericTypeAdapter::from)
            .collect(toList());
    }

}
