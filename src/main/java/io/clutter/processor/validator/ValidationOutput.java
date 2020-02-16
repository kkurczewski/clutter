package io.clutter.processor.validator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

final public class ValidationOutput {

    private final String expectation;
    private final List<ValidationOutput> violations;

    private ValidationOutput(String expectation, List<ValidationOutput> violations) {
        this.expectation = expectation;
        this.violations = violations;
    }

    public static ValidationOutput violation(String expectation, List<ValidationOutput> violations) {
        return new ValidationOutput(expectation, violations);
    }

    public static ValidationOutput violationCause(String cause) {
        return new ValidationOutput(cause, Collections.emptyList());
    }

    @Override
    public String toString() {
        return lines().stream().collect(joining(lineSeparator()));
    }

    private List<String> lines() {
        List<String> lines = new LinkedList<>();
        lines.add("\t- " + expectation);
        violations
                .stream()
                .map(ValidationOutput::lines)
                .flatMap(List::stream)
                .map("\t"::concat)
                .forEach(lines::add);

        return lines;
    }
}
