package io.clutter.javax.factory;

import io.clutter.javax.factory.common.PojoNamingConvention;
import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.method.modifiers.MethodVisibility;
import io.clutter.writer.model.param.Param;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.Set;

import static io.clutter.javax.extractor.Filters.*;
import static java.lang.String.valueOf;

final public class MethodFactory {

    /**
     * Overrides abstract method in given {@link ExecutableElement}
     *
     * @throws IllegalArgumentException when given {@link ExecutableElement} is not abstract
     */
    public static Method implement(ExecutableElement executableElement, String... body) {
        if (!ABSTRACT.test(executableElement)) {
            throw new IllegalArgumentException("ExecutableElement is not abstract");
        }
        return build(executableElement, body).setAnnotations(new AnnotationType(Override.class));
    }

    /**
     * Overrides non final method in given {@link ExecutableElement}
     * <p>
     * Prefer {@link MethodFactory#implement(ExecutableElement, String...)} to avoid accidentally overrides
     *
     * @throws IllegalArgumentException when given {@link ExecutableElement} is final
     */
    public static Method override(ExecutableElement executableElement, String... body) {
        if (FINAL.test(executableElement)) {
            throw new IllegalArgumentException("ExecutableElement is final");
        }
        return build(executableElement, body).setAnnotations(new AnnotationType(Override.class));
    }

    /**
     * Returns public getter implementation using abstract method
     *
     * @throws IllegalArgumentException when given {@link ExecutableElement} is not abstract
     */
    public static Method getter(ExecutableElement method, PojoNamingConvention convention) {
        return implement(method, "return this." + convention.variable(valueOf(method.getSimpleName())) + ";");
    }

    /**
     * Returns public getter implementation
     *
     * @throws IllegalArgumentException when given {@link VariableElement} is private
     * @throws IllegalArgumentException when given {@link VariableElement} is not field
     */
    public static Method getter(VariableElement field, PojoNamingConvention convention) {
        if (!FIELD.test(field)) {
            throw new IllegalArgumentException("VariableElement is not field");
        }
        if (PRIVATE.test(field)) {
            throw new IllegalArgumentException("VariableElement is private");
        }
        return new Method(convention.method(valueOf(field.getSimpleName())), TypeFactory.of(field.asType()))
                .setBody("return this." + field.getSimpleName() + ";");
    }

    /**
     * Returns simplest public setter implementation with void return type using abstract method
     *
     * @throws IllegalArgumentException when given {@link ExecutableElement} is not abstract
     */
    public static Method setter(ExecutableElement method, PojoNamingConvention convention) {
        String variable = convention.variable(valueOf(method.getSimpleName()));
        return implement(method, "this." + variable + " = " + variable + ";");
    }

    /**
     * Returns simplest public setter implementation with void return type
     *
     * @throws IllegalArgumentException when given {@link VariableElement} is private
     * @throws IllegalArgumentException when given {@link VariableElement} is not field
     */
    public static Method setter(VariableElement field, PojoNamingConvention convention) {
        if (!FIELD.test(field)) {
            throw new IllegalArgumentException("VariableElement is not field");
        }
        if (PRIVATE.test(field)) {
            throw new IllegalArgumentException("VariableElement is private");
        }
        String variable = valueOf(field.getSimpleName());
        return new Method(convention.method(variable), new Param(variable, valueOf(field.asType())))
                .setBody("this." + variable + " = " + variable + ";");
    }

    /**
     * @throws IllegalArgumentException when given {@link ExecutableElement} is not method
     */
    private static Method build(ExecutableElement method, String... body) {
        if (!METHOD.test(method)) {
            throw new IllegalArgumentException("ExecutableElement is not method");
        }

        Param[] params = method.getParameters()
                .stream()
                .map(javaxParam -> new Param(valueOf(javaxParam.getSimpleName()), valueOf(javaxParam.asType())))
                .toArray(Param[]::new);
        AnnotationType[] annotations = method.getAnnotationMirrors()
                .stream()
                .map(AnnotationTypeFactory::from)
                .toArray(AnnotationType[]::new);

        return new Method(valueOf(method.getSimpleName()), TypeFactory.of(method.getReturnType()), params)
                .setVisibility(visibility(method.getModifiers()))
                .setAnnotations(annotations)
                .setBody(body);
    }

    private static MethodVisibility visibility(Set<Modifier> javaxModifiers) {
        if (javaxModifiers.contains(Modifier.PUBLIC)) {
            return MethodVisibility.PUBLIC;
        } else if (javaxModifiers.contains(Modifier.PROTECTED)) {
            return MethodVisibility.PROTECTED;
        } else if (javaxModifiers.contains(Modifier.PRIVATE)) {
            throw new IllegalArgumentException("Trying to implement private method");
        } else {
            return MethodVisibility.PACKAGE_PRIVATE;
        }
    }
}
