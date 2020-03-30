package io.clutter.writer.printer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

final public class PrinterUtils {

    private PrinterUtils() {
    }

    /**
     * Joins non blank strings using given separator
     */
    public static <T> String joinNonBlank(List<T> traits, String separator) {
        return traits.stream()
                .map(String::valueOf)
                .filter(not(String::isBlank))
                .reduce((first, second) -> first + separator + second)
                .orElse("");
    }

    /**
     * Adds tabulation before every non blank line in given block
     */
    public static List<String> nested(Consumer<List<String>> nestedBlock) {
        LinkedList<String> lines = new LinkedList<>();
        nestedBlock.accept(lines);

        return lines
                .stream()
                .filter(not(String::isBlank))
                .map("\t"::concat)
                .collect(toList());
    }
}
