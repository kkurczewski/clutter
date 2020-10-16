package io.clutter.printer.z;

import java.util.List;

import static java.lang.String.join;
import static java.lang.System.lineSeparator;

public class LinesUtil {
    public static String printMultipleLines(List<String> lines) {
        if (lines.size() == 1) {
            return lines.get(0);
        } else if (lines.size() < 3) {
            return join(", ", lines);
        } else {
            return join(", " + lineSeparator(), lines) + lineSeparator();
        }
    }
}
