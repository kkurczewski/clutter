package io.clutter.printer;

import io.clutter.model.annotation.AnnotationT;
import io.clutter.model.value.ClassValue;
import io.clutter.model.value.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

final public class ValuePrinter implements ValueVisitor<LinkedList<String>> {

    private final PackagePrinter packagePrinter;
    private final AnnotationPrinter annotationPrinter;

    public ValuePrinter(AnnotationPrinter annotationPrinter, PackagePrinter packagePrinter) {
        this.packagePrinter = packagePrinter;
        this.annotationPrinter = annotationPrinter;
    }

    @Override
    public LinkedList<String> visit(AnnotationT annotationValue) {
        return annotationPrinter.print(annotationValue);
    }

    @Override
    public LinkedList<String> visit(ClassValue classValue) {
        return linkedListOf(packagePrinter.printClass(classValue.getValue()));
    }

    @Override
    public LinkedList<String> visit(EnumValue enumValue) {
        return linkedListOf(packagePrinter.printEnum(enumValue.getValue()));
    }

    @Override
    public LinkedList<String> visit(StringValue stringValue) {
        return linkedListOf("\"" + stringValue.getValue() + "\"");
    }

    @Override
    public LinkedList<String> visit(PrimitiveValue primitiveValue) {
        return linkedListOf(primitiveValue.getValue().toString());
    }

    @Override
    public LinkedList<String> visit(ListValue listValue) {
        var blocks = listValue
            .getValues()
            .stream()
            .map(value -> value.accept(this))
            .collect(toCollection(LinkedList::new));

        separateBlocks(blocks, ",");
        wrapBlocks(blocks, "{", "}");

        return flatten(blocks);
    }

    private void wrapBlocks(LinkedList<LinkedList<String>> blocks, String before, String after) {
        blocks.addFirst(linkedListOf(before));
        blocks.addLast(linkedListOf(after));
    }

    @SuppressWarnings("SameParameterValue")
    private void separateBlocks(LinkedList<LinkedList<String>> blocks, String separator) {
        var last = blocks.removeLast();
        blocks.replaceAll(block -> {
            block.add(block.removeLast() + separator);
            return block;
        });
        blocks.add(last);
    }

    private LinkedList<String> flatten(List<? extends List<String>> blocks) {
        return blocks
            .stream()
            .flatMap(Collection::stream)
            .collect(toCollection(LinkedList::new));
    }

    private LinkedList<String> linkedListOf(String value) {
        var list = new LinkedList<String>();
        list.add(value);
        return list;
    }
}
