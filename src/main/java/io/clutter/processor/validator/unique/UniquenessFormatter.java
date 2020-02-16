package io.clutter.processor.validator.unique;

import io.clutter.processor.validator.ValidationOutput;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

import static io.clutter.processor.validator.ValidationOutput.violation;

class UniquenessFormatter {

    public static ValidationOutput format(Class<? extends Annotation> nonUniqueAnnotation, List<Element> elements) {
        return violation(String.format("Expected annotation %s to be unique but following elements were annotated:", nonUniqueAnnotation.getCanonicalName()),
                elements.stream()
                        .map(element -> String.format("%s %s", toWordCase(element.getKind()), element.getSimpleName()))
                        .map(ValidationOutput::violationCause)
                        .collect(Collectors.toList()));
    }

    private static String toWordCase(Object object) {
        String stringValue = String.valueOf(object);
        return String.valueOf(stringValue.charAt(0)).toUpperCase() + stringValue.toLowerCase().substring(1);
    }
}
