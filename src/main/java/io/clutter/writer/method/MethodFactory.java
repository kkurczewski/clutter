package io.clutter.writer.method;

import io.clutter.writer.annotation.AnnotationType;
import io.clutter.writer.annotation.AnnotationTypeFactory;
import io.clutter.writer.common.PojoNamingConvention;
import io.clutter.writer.method.modifiers.MethodModifiers;
import io.clutter.writer.param.Params;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import static io.clutter.filter.Filters.*;
import static java.lang.String.valueOf;

final public class MethodFactory {

    /**
     * @throws IllegalArgumentException when given {@link ExecutableElement} is not abstract
     */
    public static Method implement(ExecutableElement executableElement, String... body) {
        if (!ABSTRACT.test(executableElement)) {
            throw new IllegalArgumentException("ExecutableElement is not abstract");
        }
        return build(executableElement, body);
    }

    /**
     * @throws IllegalArgumentException when given {@link ExecutableElement} is not final
     */
    public static Method override(ExecutableElement executableElement, String... body) {
        if (FINAL.test(executableElement)) {
            throw new IllegalArgumentException("ExecutableElement is final");
        }
        return build(executableElement, body);
    }

    /**
     * Returns public getter implementation
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
                .setBody("return this." + convention.variable(valueOf(field.getSimpleName())) + ";");
    }

    /**
     * Returns simplest public setter implementation with void return type
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
        Params params = new Params();
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

        return new Method(valueOf(method.getSimpleName()), params)
                .setModifiers(MethodModifiers.from(method.getModifiers()))
                .setReturnType(valueOf(method.getReturnType()))
                .setAnnotations(method.getAnnotationMirrors()
                        .stream()
                        .map(AnnotationTypeFactory::from)
                        .toArray(AnnotationType[]::new))
                .setBody(body);
    }
}
