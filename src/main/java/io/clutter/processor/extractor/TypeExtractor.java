package io.clutter.processor.extractor;

import io.clutter.filter.ElementFilter;
import io.clutter.filter.MethodFilter;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.clutter.filter.Filters.FIELD;
import static io.clutter.filter.Filters.METHOD;

final public class TypeExtractor {

    private final String typeQualifiedName;
    private final TypeElement rootElement;

    public TypeExtractor(TypeElement rootElement) {
        this.typeQualifiedName = rootElement.getQualifiedName().toString();
        this.rootElement = rootElement;
    }

    public TypeElement extractRootElement() {
        return rootElement;
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

    public String getTypeQualifiedName() {
        return typeQualifiedName;
    }

    private <T extends Element> Predicate<T> composeFilters(Predicate<T>[] filters) {
        return Stream.of(filters)
                .reduce(Predicate::and)
                .orElse((executableElement) -> true);
    }
}
