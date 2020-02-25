package io.clutter.javax.filter;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import java.lang.annotation.Annotation;

import static javax.lang.model.type.TypeKind.VOID;

/**
 * Common predicates to work with {@link javax.lang.model.element.Element} types
 */
final public class Filters {

    public static final ElementFilter FIELD = (element) -> element.getKind() == ElementKind.FIELD;
    public static final ElementFilter METHOD = (element) -> element.getKind() == ElementKind.METHOD;
    public static final ElementFilter INTERFACE = (element) -> element.getKind() == ElementKind.INTERFACE;
    public static final ElementFilter CLASS = (element) -> element.getKind() == ElementKind.CLASS;
    public static final ElementFilter FINAL = (element) -> element.getModifiers().contains(Modifier.FINAL);
    public static final ElementFilter PRIVATE = (element) -> element.getModifiers().contains(Modifier.PRIVATE);

    public static final MethodFilter NO_ARG = methodFilter((m) -> m.getParameters().isEmpty());
    public static final MethodFilter SINGLE_ARG = methodFilter((m) -> m.getParameters().size() == 1);
    public static final MethodFilter VOID_RETURN = methodFilter((m) -> m.getReturnType().getKind() == VOID);
    public static final MethodFilter ACCESSOR = methodFilter((m) -> NO_ARG.test(m) && !VOID_RETURN.test(m));
    public static final MethodFilter ABSTRACT = methodFilter((m) -> !m.isDefault() || m.getModifiers().contains(Modifier.ABSTRACT));

    public static ElementFilter isAnnotated(Class<? extends Annotation> annotation) {
        return element -> element.getAnnotation(annotation) != null;
    }

    public static MethodFilter methodFilter(MethodFilter methodFilter) {
        return element -> METHOD.test(element) && methodFilter.test(element);
    }
}
