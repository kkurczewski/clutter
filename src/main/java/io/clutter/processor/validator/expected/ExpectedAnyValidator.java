package io.clutter.processor.validator.expected;

import io.clutter.javax.extractor.TypeExtractor;
import io.clutter.javax.extractor.Filters;
import io.clutter.processor.validator.TypeValidator;
import io.clutter.processor.validator.ValidationOutput;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final public class ExpectedAnyValidator implements TypeValidator {

    private Set<Class<? extends Annotation>> annotations;

    public ExpectedAnyValidator(Set<Class<? extends Annotation>> annotations) {
        this.annotations = annotations;
    }

    @Override
    public List<ValidationOutput> validate(TypeElement type) {
        TypeExtractor typeExtractor = new TypeExtractor(type);
        boolean constraintViolated = annotations
                .stream()
                .map(Filters::isAnnotated)
                .map(typeExtractor::extractElements)
                .allMatch(List::isEmpty);

        if (constraintViolated) {
            return List.of(ExpectedAnyFormatter.format(annotations));
        }
        return new ArrayList<>();
    }
}
