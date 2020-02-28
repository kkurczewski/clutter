package io.clutter.javax.extractor;

import io.clutter.javax.filter.ElementFilter;
import io.clutter.javax.filter.MethodFilter;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.clutter.javax.filter.Filters.FIELD;
import static io.clutter.javax.filter.Filters.METHOD;

/**
 * Provides simplified API to extract {@link Element} types from {@link TypeElement}
 *
 * @see VariableElement
 * @see ExecutableElement
 * @see io.clutter.javax.filter.Filters
 */
final public class TypeExtractor {

    private final TypeElement rootElement;

    public TypeExtractor(TypeElement rootElement) {
        this.rootElement = rootElement;
    }

    public List<Element> extractElements(ElementFilter... elementFilters) {
        Predicate<Element> composedFilter = composeFilters(elementFilters);
        return rootElement
                .getEnclosedElements()
                .stream()
                .filter(composedFilter)
                .collect(Collectors.toList());
    }

    public List<VariableElement> extractFields(ElementFilter... fieldFilters) {
        Predicate<Element> composedFilter = composeFilters(fieldFilters);
        return extractElements()
                .stream()
                .filter(FIELD)
                .map(VariableElement.class::cast)
                .filter(composedFilter)
                .collect(Collectors.toList());
    }

    public List<ExecutableElement> extractMethods(MethodFilter... methodFilters) {
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
