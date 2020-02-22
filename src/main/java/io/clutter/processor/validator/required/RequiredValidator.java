package io.clutter.processor.validator.required;

import io.clutter.javax.extractor.TypeExtractor;
import io.clutter.javax.filter.Filters;
import io.clutter.processor.validator.TypeValidator;
import io.clutter.processor.validator.ValidationOutput;

import javax.lang.model.element.TypeElement;
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
    public List<ValidationOutput> validate(TypeElement type) {
        TypeExtractor typeExtractor = new TypeExtractor(type);
        return annotations
                .stream()
                .filter(annotation -> typeExtractor.extractElements(Filters.isAnnotated(annotation)).isEmpty())
                .map(RequiredFormatter::format)
                .collect(Collectors.toList());
    }
}
