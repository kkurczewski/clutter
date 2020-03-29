package io.clutter.javax.extractor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.clutter.javax.extractor.Filters.*;
import static io.clutter.javax.extractor.Filters.FIELD;
import static io.clutter.javax.extractor.Filters.METHOD;

/**
 * Provides simplified API to extract {@link Element} types from {@link TypeElement}
 *
 * @see VariableElement
 * @see ExecutableElement
 * @see Filters
 */
final public class TypeExtractor {

    private final TypeElement rootElement;

    public TypeExtractor(TypeElement rootElement) {
        this.rootElement = rootElement;
    }

    public TypeElement rootElement() {
        return rootElement;
    }

    @SafeVarargs
    final public List<Element> extractElements(Predicate<Element>... elementFilters) {
        Predicate<Element> composedFilter = composeFilters(elementFilters);
        return rootElement
                .getEnclosedElements()
                .stream()
                .filter(composedFilter)
                .collect(Collectors.toList());
    }

    @SafeVarargs
    final public List<ExecutableElement> extractConstructor(Predicate<ExecutableElement>... fieldFilters) {
        Predicate<ExecutableElement> composedFilter = composeFilters(fieldFilters);
        return extractElements()
                .stream()
                .filter(CONSTRUCTOR)
                .map(ExecutableElement.class::cast)
                .filter(composedFilter)
                .collect(Collectors.toList());
    }

    @SafeVarargs
    final public List<VariableElement> extractFields(Predicate<VariableElement>... fieldFilters) {
        Predicate<VariableElement> composedFilter = composeFilters(fieldFilters);
        return extractElements()
                .stream()
                .filter(FIELD)
                .map(VariableElement.class::cast)
                .filter(composedFilter)
                .collect(Collectors.toList());
    }

    @SafeVarargs
    final public List<ExecutableElement> extractMethods(Predicate<ExecutableElement>... methodFilters) {
        Predicate<ExecutableElement> composedFilter = composeFilters(methodFilters);
        return extractElements()
                .stream()
                .filter(METHOD)
                .map(ExecutableElement.class::cast)
                .filter(composedFilter)
                .collect(Collectors.toList());
    }

    private <T extends Element> Predicate<T> composeFilters(Predicate<T>[] filters) {
        return Stream.of(filters)
                .reduce(Predicate::and)
                .orElse((element) -> true);
    }
}
