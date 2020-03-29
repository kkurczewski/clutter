package io.clutter.javax.factory;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.method.Method;
import io.clutter.model.method.modifiers.MethodVisibility;
import io.clutter.model.param.Param;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.Set;

import static io.clutter.javax.extractor.Filters.METHOD;
import static java.lang.String.valueOf;

final public class MethodFactory {

    /**
     * Creates method from {@link ExecutableElement}
     *
     * @throws IllegalArgumentException when given {@link ExecutableElement} is not method
     */
    public static Method from(ExecutableElement method, String... body) {
        if (!METHOD.test(method)) {
            throw new IllegalArgumentException("ExecutableElement is not method");
        }

        Param[] params = method.getParameters()
                .stream()
                .map(javaxParam -> Param.of(valueOf(javaxParam.getSimpleName()), TypeFactory.from(javaxParam.asType())))
                .toArray(Param[]::new);
        AnnotationType[] annotations = method.getAnnotationMirrors()
                .stream()
                .map(AnnotationFactory::from)
                .toArray(AnnotationType[]::new);

        return new Method(valueOf(method.getSimpleName()), TypeFactory.from(method.getReturnType()), params)
                .setVisibility(mapJavaxVisibility(method.getModifiers()))
                .setAnnotations(annotations)
                .setBody(body);
    }

    private static MethodVisibility mapJavaxVisibility(Set<Modifier> javaxModifiers) {
        if (javaxModifiers.contains(Modifier.PUBLIC)) {
            return MethodVisibility.PUBLIC;
        } else if (javaxModifiers.contains(Modifier.PROTECTED)) {
            return MethodVisibility.PROTECTED;
        } else if (javaxModifiers.contains(Modifier.PRIVATE)) {
            throw new IllegalArgumentException("Attempt to implement private method");
        } else {
            return MethodVisibility.PACKAGE_PRIVATE;
        }
    }
}
