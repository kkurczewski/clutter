package io.clutter.printer;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.annotation.param.AnnotationValue;
import io.clutter.javax.factory.annotation.AnnotationFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

final class AnnotationPrinter {

    private final TypePrinter typePrinter;

    public AnnotationPrinter(TypePrinter typePrinter) {
        this.typePrinter = typePrinter;
    }

    public List<String> print(AnnotationType annotationType) {
        List<String> lines = new LinkedList<>();
        lines.add("@" + printType(annotationType.getType()) + params(annotationType));
        return lines;
    }

    private Object params(AnnotationType annotationType) {
        return annotationType
                .getParams()
                .entrySet()
                .stream()
                .map(param -> param.getKey() + " = " + printAnnotationValue(param.getValue()))
                .reduce((first, second) -> first + ", " + second)
                .map(params -> "(" + params + ")")
                .orElse("");
    }

    private String printAnnotationValue(AnnotationValue value) {
        Object object = value.getValue();
        if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            return IntStream.range(0, length)
                    .mapToObj(i -> printRawValue(Array.get(object, i)))
                    .collect(joining(", ", "{", "}"));
        }
        return printRawValue(object);
    }

    private String printRawValue(Object object) {
        if (object instanceof String) {
            return "\"" + object.toString() + "\"";
        } else if (object instanceof Enum) {
            return printEnum((Enum<?>) object);
        } else if (object instanceof Annotation) {
            return String.join("", print(AnnotationFactory.from((Annotation) object)));
        } else if (object instanceof Class) {
            return printType((Class<?>) object) + ".class";
        }
        return object.toString();
    }

    private String printEnum(Enum<?> object) {
        return typePrinter.print(object);
    }

    private String printType(Class<?> type) {
        return typePrinter.print(type);
    }
}
