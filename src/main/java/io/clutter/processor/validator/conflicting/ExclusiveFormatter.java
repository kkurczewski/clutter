package io.clutter.processor.validator.conflicting;

import io.clutter.processor.validator.ValidationOutput;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

import static io.clutter.processor.validator.ValidationOutput.*;

class ExclusiveFormatter {
    public static ValidationOutput format(Element element, Set<Annotation> conflictedAnnotations) {
        return violation(String.format("%s %s has exclusive annotations:",
                toWordCase(element.getKind()),
                element.getSimpleName()),
                conflictedAnnotations
                        .stream()
                        .map(Annotation::annotationType)
                        .map(Class::getCanonicalName)
                        .sorted()
                        .map(ValidationOutput::violationCause)
                        .collect(Collectors.toList())
        );
    }

    private static String toWordCase(Object object) {
        String stringValue = String.valueOf(object);
        return String.valueOf(stringValue.charAt(0)).toUpperCase() + stringValue.toLowerCase().substring(1);
    }
}
