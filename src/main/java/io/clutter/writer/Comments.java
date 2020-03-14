package io.clutter.writer;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static io.clutter.common.Varargs.concat;

final public class Comments {

    private List<String> lines;

    public Comments(List<String> lines) {
        this.lines = lines;
    }

    public static Comments javadoc(String comment, String... more) {
        List<String> lines = new LinkedList<>();
        lines.add("/**");
        concat(comment, more)
                .stream()
                .map(" * "::concat)
                .forEach(lines::add);
        lines.add(" */");
        return new Comments(lines);
    }

    public static Comments comment(String comment, String... more) {
        return new Comments(concat(comment, more)
                .stream()
                .map("// "::concat)
                .collect(Collectors.toList()));
    }

    public static Comments multiLineComment(String comment, String... more) {
        List<String> lines = new LinkedList<>();
        lines.add("/*");
        concat(comment, more)
                .stream()
                .map(" "::concat)
                .forEach(lines::add);
        lines.add(" */");
        return new Comments(lines);
    }

    public List<String> getLines() {
        return lines;
    }
}
