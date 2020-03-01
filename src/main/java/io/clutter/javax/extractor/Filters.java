package io.clutter.javax.extractor;

import io.clutter.common.Varargs;

import javax.lang.model.element.*;
import java.lang.annotation.Annotation;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static javax.lang.model.type.TypeKind.VOID;

/**
 * Common predicates to work with {@link javax.lang.model.element.Element} types
 */
final public class Filters {

    public static final Predicate<Element> FIELD = (element) -> element.getKind() == ElementKind.FIELD;
    public static final Predicate<Element> METHOD = (element) -> element.getKind() == ElementKind.METHOD;
    public static final Predicate<Element> INTERFACE = (element) -> element.getKind() == ElementKind.INTERFACE;
    public static final Predicate<Element> CLASS = (element) -> element.getKind() == ElementKind.CLASS;
    public static final Predicate<Element> FINAL = (element) -> element.getModifiers().contains(Modifier.FINAL);
    public static final Predicate<Element> PRIVATE = (element) -> element.getModifiers().contains(Modifier.PRIVATE);

    public static final Predicate<VariableElement> OPEN_PROPERTY = (field) -> !field.getModifiers().contains(Modifier.PRIVATE) && field.getConstantValue() == null;

    public static final Predicate<ExecutableElement> NO_ARG = methodFilter((m) -> m.getParameters().isEmpty());
    public static final Predicate<ExecutableElement> SINGLE_ARG = methodFilter((m) -> m.getParameters().size() == 1);
    public static final Predicate<ExecutableElement> VOID_RETURN = methodFilter((m) -> m.getReturnType().getKind() == VOID);
    public static final Predicate<ExecutableElement> ACCESSOR = methodFilter((m) -> NO_ARG.test(m) && !VOID_RETURN.test(m));
    public static final Predicate<ExecutableElement> ABSTRACT = methodFilter((m) -> !m.isDefault() || m.getModifiers().contains(Modifier.ABSTRACT));

    @SafeVarargs
    public static Predicate<Element> isAnnotated(Class<? extends Annotation> annotation, Class<? extends Annotation>... more) {
        return element -> Stream.of(Varargs.concat(annotation, more))
                .anyMatch(a -> element.getAnnotation(a) != null);
    }

    public static Predicate<ExecutableElement> methodFilter(Predicate<ExecutableElement> methodFilter) {
        return element -> METHOD.test(element) && methodFilter.test(element);
    }
}
