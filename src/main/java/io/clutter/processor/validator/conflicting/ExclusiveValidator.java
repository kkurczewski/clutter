package io.clutter.processor.validator.conflicting;

import io.clutter.javax.extractor.TypeExtractor;
import io.clutter.processor.validator.TypeValidator;
import io.clutter.processor.validator.ValidationOutput;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

final public class ExclusiveValidator implements TypeValidator {

    private Set<Class<? extends Annotation>> annotations;

    public ExclusiveValidator(Set<Class<? extends Annotation>> annotations) {
        this.annotations = annotations;
    }

    @Override
    public List<ValidationOutput> validate(TypeElement type) {
        TypeExtractor typeExtractor = new TypeExtractor(type);
        var conflicts = typeExtractor
                .extractElements()
                .stream()
                .collect(toMap(Function.identity(), this::collectElementAnnotations));

        conflicts.put(type, collectElementAnnotations(type));

        return conflicts.entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(entry -> ExclusiveFormatter.format(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private Set<Annotation> collectElementAnnotations(Element rootElement) {
        return annotations
                .stream()
                .map(rootElement::getAnnotation)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
