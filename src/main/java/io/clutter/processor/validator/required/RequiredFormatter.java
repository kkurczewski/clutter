package io.clutter.processor.validator.required;

import io.clutter.processor.validator.ValidationOutput;

import java.lang.annotation.Annotation;
import java.util.List;

import static io.clutter.processor.validator.ValidationOutput.violation;
import static io.clutter.processor.validator.ValidationOutput.violationCause;

final class RequiredFormatter {
    public static ValidationOutput format(Class<? extends Annotation> annotation) {
        return violation("Expected annotations:",
                List.of(violationCause(annotation.getCanonicalName()))
        );
    }
}
