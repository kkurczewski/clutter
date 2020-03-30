package io.clutter.javax.factory;

import io.clutter.javax.extractor.TypeExtractor;
import io.clutter.javax.factory.annotation.AnnotationFactory;
import io.clutter.javax.factory.types.BoxedTypeFactory;
import io.clutter.javax.factory.types.WildcardTypeFactory;
import io.clutter.model.classtype.ClassType;
import io.clutter.model.classtype.modifiers.ClassTrait;
import io.clutter.model.classtype.modifiers.ClassVisibility;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.field.Field;
import io.clutter.model.method.Method;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.WildcardType;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Objects;
import java.util.Set;

import static io.clutter.model.classtype.modifiers.ClassVisibility.*;

final public class ClassFactory {

    public static ClassType from(TypeElement classType) {
        if (classType.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException(classType.getKind().name());
        }

        var className = classType.getQualifiedName().toString();
        var modifiers = classType.getModifiers();
        var extractor = new TypeExtractor(classType);
        var type = BoxedTypeFactory.from(classType.getSuperclass());
        return new ClassType(className)
                .setAnnotations(AnnotationFactory.from(classType))
                .setGenericParameters(generics(classType))
                .setVisibility(visibility(modifiers))
                .setTraits(traits(modifiers))
                .setInterfaces(interfaces(classType))
                .setSuperclass(type.getType() != Object.class ? type : null)
                .setFields(fields(extractor))
                .setConstructors(constructors(extractor))
                .setMethods(methods(extractor));
    }

    private static Method[] methods(TypeExtractor extractor) {
        return extractor.extractMethods()
                .stream()
                .map(MethodFactory::from)
                .toArray(Method[]::new);
    }

    private static Constructor[] constructors(TypeExtractor extractor) {
        return extractor.extractConstructors()
                .stream()
                .map(ConstructorFactory::from)
                .toArray(Constructor[]::new);
    }

    private static Field[] fields(TypeExtractor extractor) {
        return extractor.extractFields()
                .stream()
                .map(FieldFactory::from)
                .toArray(Field[]::new);
    }

    private static BoxedType[] interfaces(TypeElement typeElement) {
        return typeElement.getInterfaces()
                .stream()
                .map(BoxedTypeFactory::from)
                .toArray(BoxedType[]::new);
    }

    private static WildcardType[] generics(TypeElement typeElement) {
        return typeElement.getTypeParameters()
                .stream()
                .map(Element::asType)
                .map(WildcardTypeFactory::from)
                .toArray(WildcardType[]::new);
    }

    private static ClassTrait[] traits(Set<Modifier> modifiers) {
        return modifiers.stream().map(modifier -> {
            switch (modifier) {
                case ABSTRACT:
                    return ClassTrait.ABSTRACT;
                case FINAL:
                    return ClassTrait.FINAL;
                case STATIC:
                    return ClassTrait.STATIC;
                default:
                    return null;
            }
        }).filter(Objects::nonNull).toArray(ClassTrait[]::new);
    }

    private static ClassVisibility visibility(Set<Modifier> modifiers) {
        return modifiers.stream().map(visibility -> {
            switch (visibility) {
                case PUBLIC:
                    return PUBLIC;
                case PROTECTED:
                    return PROTECTED;
                case PRIVATE:
                    return PRIVATE;
                default:
                    return null;
            }
        }).findFirst().orElse(PACKAGE_PRIVATE);
    }
}
