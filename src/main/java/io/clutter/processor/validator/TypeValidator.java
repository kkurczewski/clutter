package io.clutter.processor.validator;

import io.clutter.processor.extractor.TypeExtractor;

import java.util.List;

@FunctionalInterface
public interface TypeValidator {
    List<ValidationOutput> validate(TypeExtractor typeExtractor);

    default TypeValidator compose(TypeValidator other) {
        return typeExtractor -> {
            List<ValidationOutput> failures = this.validate(typeExtractor);
            failures.addAll(other.validate(typeExtractor));
            return failures;
        };
    }
}
