package io.clutter.printer;

import io.clutter.model.annotation.AnnotationT;
import io.clutter.model.value.Value;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toCollection;

final public class AnnotationPrinter {

    private final ValuePrinter valuePrinter;
    private final PackagePrinter packagePrinter;

    public AnnotationPrinter() {
        this.packagePrinter = new PackagePrinter();
        this.valuePrinter = new ValuePrinter(this, this.packagePrinter);
    }

    public LinkedList<String> print(List<AnnotationT> annotation) {
        return annotation.stream()
            .map(this::print)
            .flatMap(Collection::stream)
            .collect(toCollection(LinkedList::new));
    }

    public LinkedList<String> print(AnnotationT annotation) {
        var list = new LinkedList<String>();
        list.add("@" + packagePrinter.printClass(annotation.getType().getType()));
        if (!annotation.getParams().isEmpty()) {
            list.add("(");
            list.addAll(printParams(annotation.getParams()));
            list.add(")");
        }
        return list;
    }

    private LinkedList<String> printParams(Map<String, Value> params) {
        var blocks = params
            .entrySet()
            .stream()
            .map(this::printParam)
            .collect(toCollection(LinkedList::new));

        separateBlocks(blocks, ",");

        return flatten(blocks);
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

    private LinkedList<String> printParam(Map.Entry<String, Value> param) {
        var printedParam = param.getValue().accept(valuePrinter);
        if (!printedParam.isEmpty()) {
            printedParam.addFirst(param.getKey() + " = " + printedParam.removeFirst());
        }
        return printedParam;
    }

}