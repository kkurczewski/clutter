package io.clutter.processor.validator.unique;

import io.clutter.processor.extractor.TypeExtractor;
import io.clutter.processor.validator.TypeValidator;
import io.clutter.processor.validator.ValidationOutput;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

final public class UniquenessValidator implements TypeValidator {

    private final Set<Class<? extends Annotation>> annotations;

    public UniquenessValidator(Set<Class<? extends Annotation>> annotations) {
        this.annotations = annotations;
    }

    @Override
    public List<ValidationOutput> validate(TypeExtractor typeExtractor) {
        Map<Class<? extends Annotation>, List<Element>> conflicts = annotations
                .stream()
                .collect(Collectors.toMap(Function.identity(), typeExtractor::extractAnnotatedElements));

        TypeElement rootElement = typeExtractor.extractRootElement();
        annotations
                .stream()
                .filter(typeExtractor::isClassAnnotated)
                .forEach(s -> conflicts.merge(s, List.of(rootElement), this::concat));

        return conflicts
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(entry -> UniquenessFormatter.format(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private List<Element> concat(List<Element> first, List<Element> second) {
        first.addAll(second);
        return first;
    }
}
