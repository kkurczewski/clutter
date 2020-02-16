package io.clutter.processor.validator.required;

import io.clutter.processor.extractor.TypeExtractor;
import io.clutter.processor.validator.TypeValidator;
import io.clutter.processor.validator.ValidationOutput;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

final public class RequiredValidator implements TypeValidator {

    private Set<Class<? extends Annotation>> annotations;

    public RequiredValidator(Set<Class<? extends Annotation>> annotations) {
        this.annotations = annotations;
    }

    @Override
    public List<ValidationOutput> validate(TypeExtractor typeExtractor) {
        return annotations
                .stream()
                .filter(annotation -> typeExtractor.extractAnnotatedElements(annotation).isEmpty())
                .map(RequiredFormatter::format)
                .collect(Collectors.toList());
    }
}
