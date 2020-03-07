package io.clutter.processor.validator.conflicting;

import io.clutter.processor.validator.ValidationOutput;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

import static io.clutter.javax.factory.common.StringUtils.toPascalCase;
import static io.clutter.processor.validator.ValidationOutput.violation;
import static java.lang.String.valueOf;

final class ExclusiveFormatter {
    public static ValidationOutput format(Element element, Set<Annotation> conflictedAnnotations) {
        return violation(String.format("%s %s has exclusive annotations:",
                toPascalCase(valueOf(element.getKind()).toLowerCase()),
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
}
