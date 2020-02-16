package io.clutter.processor.validator;

import io.clutter.processor.validator.conflicting.ExclusiveValidator;
import io.clutter.processor.validator.expected.ExpectedAnyValidator;
import io.clutter.processor.validator.required.RequiredValidator;
import io.clutter.processor.validator.unique.UniquenessValidator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

final public class AnnotationValidatorBuilder {

    private final List<TypeValidator> validators = new ArrayList<>();

    /**
     * Mark annotation in set as unique
     */
    public AnnotationValidatorBuilder unique(Set<Class<? extends Annotation>> annotations) {
        validators.add(new UniquenessValidator(annotations));
        return this;
    }

    /**
     * Expect all of annotation in set to be used
     */
    public AnnotationValidatorBuilder required(Set<Class<? extends Annotation>> annotations) {
        validators.add(new RequiredValidator(annotations));
        return this;
    }

    /**
     * Expect one of annotation in set to be used
     */
    public AnnotationValidatorBuilder expectAny(Set<Class<? extends Annotation>> annotations) {
        validators.add(new ExpectedAnyValidator(annotations));
        return this;
    }

    /**
     * Forbid of mixing annotations from set (on element level)
     */
    public AnnotationValidatorBuilder exclusive(Set<Class<? extends Annotation>> annotations) {
        validators.add(new ExclusiveValidator(annotations));
        return this;
    }

    public TypeValidator build() {
        return typeExtractor -> validators
                .stream()
                .reduce(TypeValidator::compose)
                .orElse($ -> Collections.emptyList())
                .validate(typeExtractor);
    }
}
