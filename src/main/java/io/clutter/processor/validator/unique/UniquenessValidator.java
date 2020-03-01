package io.clutter.processor.validator.unique;

import io.clutter.javax.extractor.TypeExtractor;
import io.clutter.javax.extractor.Filters;
import io.clutter.processor.validator.TypeValidator;
import io.clutter.processor.validator.ValidationOutput;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

final public class UniquenessValidator implements TypeValidator {

    private final Set<Class<? extends Annotation>> annotations;

    public UniquenessValidator(Set<Class<? extends Annotation>> annotations) {
        this.annotations = annotations;
    }

    @Override
    public List<ValidationOutput> validate(TypeElement type) {
        TypeExtractor typeExtractor = new TypeExtractor(type);
        List<Element> elements = typeExtractor.extractElements();
        elements.add(type);

        var conflicts = annotations
                .stream()
                .collect(toMap(
                        Function.identity(),
                        annotation -> elements.stream()
                                .filter(Filters.isAnnotated(annotation))
                                .collect(toList())));

        return conflicts
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(entry -> UniquenessFormatter.format(entry.getKey(), entry.getValue()))
                .collect(toList());
    }
}
