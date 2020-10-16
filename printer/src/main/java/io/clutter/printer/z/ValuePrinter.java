package io.clutter.printer.z;

import io.clutter.model.z.model.value.ClassValue;
import io.clutter.model.z.model.value.*;

import java.util.LinkedList;
import java.util.List;

public class ValuePrinter implements ValueVisitor<List<String>> {

    private final ClassPrinter classPrinter;
    private final AnnotationPrinter annotationPrinter;

    public ValuePrinter(AnnotationPrinter annotationPrinter, ClassPrinter classPrinter) {
        this.classPrinter = classPrinter;
        this.annotationPrinter = annotationPrinter;
    }

    @Override
    public List<String> visit(AnnotationValue annotationValue) {
        return annotationPrinter.print(annotationValue.getValue());
    }

    @Override
    public List<String> visit(ClassValue classValue) {
        return listValue(classPrinter.printClass(classValue.getValue()));
    }

    @Override
    public List<String> visit(EnumValue enumValue) {
        return listValue(classPrinter.printEnum(enumValue.getValue()));
    }

    @Override
    public List<String> visit(StringValue stringValue) {
        return listValue("\"" + stringValue.getValue() + "\"");
    }

    @Override
    public List<String> visit(PrimitiveValue primitiveValue) {
        return listValue(primitiveValue.getValue().toString());
    }

    private List<String> listValue(String value) {
        List<String> list = new LinkedList<>();
        list.add(value);
        return list;
    }
}
