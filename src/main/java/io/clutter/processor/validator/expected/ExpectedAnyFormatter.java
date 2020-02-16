package io.clutter.processor.validator.expected;

import io.clutter.processor.validator.ValidationOutput;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

import static io.clutter.processor.validator.ValidationOutput.violation;

class ExpectedAnyFormatter {
    public static ValidationOutput format(Set<Class<? extends Annotation>> annotations) {
        return violation("Expected one of the following annotations:",
                annotations
                        .stream()
                        .map(Class::getCanonicalName)
                        .sorted()
                        .map(ValidationOutput::violationCause)
                        .collect(Collectors.toList()));
    }
}
