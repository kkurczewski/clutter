package io.clutter.printer.z;

import io.clutter.model.z.model.annotation.Annotation;
import io.clutter.model.z.model.value.Value;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toCollection;

public class AnnotationPrinter {

    private final ValuePrinter valuePrinter;
    private final ClassPrinter classPrinter;

    public AnnotationPrinter() {
        this.classPrinter = new ClassPrinter();
        this.valuePrinter = new ValuePrinter(this, this.classPrinter);
    }

    public List<String> print(Annotation annotation) {
        List<String> list = new LinkedList<>();
        list.add("@" + classPrinter.printClass(annotation.getType()));
        if (!annotation.getParams().isEmpty()) {
            list.add("(");
            list.addAll(printParams(annotation.getParams()));
            list.add(")");
        }
        return list;
    }

    private LinkedList<String> printParams(Map<String, List<Value>> params) {
        return params
            .entrySet()
            .stream()
            .map(this::printParam)
            .reduce(this::mergeParams)
            .orElse(new LinkedList<>());
    }

    private LinkedList<String> mergeParams(LinkedList<String> firstParam, LinkedList<String> secondParam) {
        appendCommaToBlock(firstParam);
        firstParam.addAll(secondParam);
        return firstParam;
    }

    private LinkedList<String> printParam(Map.Entry<String, List<Value>> param) {
        var paramValues = printBlocks(param.getValue());
        if (!paramValues.isEmpty()) {
            paramValues.addFirst(param.getKey() + " = " + paramValues.remove(0));
        }
        return paramValues;
    }

    private LinkedList<String> printBlocks(List<Value> values) {
        var blocks = values
            .stream()
            .map(value -> value.accept(valuePrinter))
            .map(LinkedList::new)
            .collect(toCollection(LinkedList::new));

        if (blocks.size() > 1) {
            splitBlocksWithComa(blocks);

            blocks.addFirst(linkedListOf("{"));
            blocks.addLast(linkedListOf("}"));
        }

        return flattenBlocks(blocks);
    }

    private void splitBlocksWithComa(LinkedList<LinkedList<String>> blocks) {
        var last = blocks.removeLast();
        blocks.replaceAll(this::appendCommaToBlock);
        blocks.addLast(last);
    }

    private LinkedList<String> appendCommaToBlock(LinkedList<String> block) {
        var last = block.removeLast() + ",";
        block.addLast(last);
        return block;
    }

    private LinkedList<String> flattenBlocks(LinkedList<LinkedList<String>> blocks) {
        return blocks
            .stream()
            .flatMap(Collection::stream)
            .collect(toCollection(LinkedList::new));
    }

    private LinkedList<String> linkedListOf(String value) {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add(value);
        return linkedList;
    }
}