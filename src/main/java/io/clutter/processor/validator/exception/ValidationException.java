package io.clutter.processor.validator.exception;

import io.clutter.processor.exception.AnnotationProcessorException;
import io.clutter.processor.validator.ValidationOutput;

import java.util.List;
import java.util.Map;

import static io.clutter.processor.validator.ValidationOutput.violation;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

final public class ValidationException extends AnnotationProcessorException {

    public ValidationException(Map<String, List<ValidationOutput>> violations) {
        super(composeMessage(violations));
    }

    private static String composeMessage(Map<String, List<ValidationOutput>> classViolations) {
        return classViolations
                .entrySet()
                .stream()
                .map((entry) -> violation(format("Class %s has following violations:", entry.getKey()), entry.getValue()))
                .map(ValidationOutput::toString)
                .collect(joining(System.lineSeparator()));
    }
}
