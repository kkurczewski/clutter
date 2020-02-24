package io.clutter.javax.factory;

import io.clutter.writer.common.PojoNamingConvention;
import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.method.modifiers.MethodModifiers;
import io.clutter.writer.model.method.modifiers.MethodVisibility;
import io.clutter.writer.model.param.Params;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.LinkedHashSet;
import java.util.Set;

import static io.clutter.javax.filter.Filters.*;
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
        return new Method(convention.method(valueOf(field.getSimpleName())), new Params())
                .setModifiers(MethodModifiers.PUBLIC)
                .setReturnType(valueOf(field.asType()))
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
        Params params = new Params();
        String variable = valueOf(field.getSimpleName());
        params.add(variable, valueOf(field.asType()));

        return new Method(convention.method(variable), params)
                .setModifiers(MethodModifiers.PUBLIC)
                .setBody("this." + variable + " = " + variable + ";");
    }

    /**
     * @throws IllegalArgumentException when given {@link ExecutableElement} is not method
     */
    private static Method build(ExecutableElement method, String... body) {
        if (!METHOD.test(method)) {
            throw new IllegalArgumentException("ExecutableElement is not method");
        }
        Params params = new Params();
        method.getParameters()
                .forEach(param -> params.add(valueOf(param.getSimpleName()), valueOf(param.asType())));

        LinkedHashSet<Modifier> javaxModifiers = new LinkedHashSet<>(method.getModifiers());
        javaxModifiers.remove(Modifier.ABSTRACT);

        return new Method(valueOf(method.getSimpleName()), params)
                .setModifiers(modifiers(javaxModifiers))
                .setReturnType(valueOf(method.getReturnType()))
                .setAnnotations(method.getAnnotationMirrors()
                        .stream()
                        .map(AnnotationTypeFactory::from)
                        .toArray(AnnotationType[]::new))
                .setBody(body);
    }

    private static MethodModifiers modifiers(Set<Modifier> javaxModifiers) {
        final MethodVisibility visibility;
        if (javaxModifiers.contains(Modifier.PUBLIC)) {
            visibility = MethodVisibility.PUBLIC;
        } else if (javaxModifiers.contains(Modifier.PROTECTED)) {
            visibility = MethodVisibility.PROTECTED;
        } else if (javaxModifiers.contains(Modifier.PRIVATE)) {
            throw new IllegalArgumentException("Trying to implement private method");
        } else {
            visibility = MethodVisibility.PACKAGE_PRIVATE;
        }

        return new MethodModifiers(visibility);
    }
}
