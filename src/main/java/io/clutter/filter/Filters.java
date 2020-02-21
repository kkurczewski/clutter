package io.clutter.filter;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import java.lang.annotation.Annotation;

final public class Filters {

    public static final ElementFilter FIELD = (element) -> element.getKind() == ElementKind.FIELD;
    public static final ElementFilter METHOD = (element) -> element.getKind() == ElementKind.METHOD;
    public static final ElementFilter INTERFACE = (element) -> element.getKind() == ElementKind.INTERFACE;
    public static final ElementFilter CLASS = (element) -> element.getKind() == ElementKind.CLASS;
    public static final ElementFilter FINAL = (element) -> element.getModifiers().contains(Modifier.FINAL);
    public static final ElementFilter PRIVATE = (element) -> element.getModifiers().contains(Modifier.PRIVATE);

    public static final MethodFilter NO_ARG = (method) -> method.getParameters().isEmpty();
    public static final MethodFilter SINGLE_ARG = (method) -> method.getParameters().size() == 1;
    public static final MethodFilter VOID_RETURN = (method) -> method.getReturnType().getKind() == TypeKind.VOID;
    public static final MethodFilter ACCESSOR = (method) -> NO_ARG.test(method) && !VOID_RETURN.test(method);
    public static final MethodFilter ABSTRACT = (method) -> !method.isDefault() && method.getModifiers().contains(Modifier.ABSTRACT);

    public static ElementFilter annotated(Class<? extends Annotation> annotation) {
        return element -> element.getAnnotation(annotation) != null;
    }
}
