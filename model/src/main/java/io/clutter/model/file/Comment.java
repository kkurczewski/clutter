package io.clutter.model.file;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static io.clutter.model.util.Varargs.concat;

final public class Comment {

    private List<String> lines;

    public Comment(List<String> lines) {
        this.lines = lines;
    }

    public static Comment javadoc(String comment, String... more) {
        List<String> lines = new LinkedList<>();
        lines.add("/**");
        concat(comment, more)
                .stream()
                .map(" * "::concat)
                .forEach(lines::add);
        lines.add(" */");
        return new Comment(lines);
    }

    public static Comment comment(String comment, String... more) {
        return new Comment(concat(comment, more)
                .stream()
                .map("// "::concat)
                .collect(Collectors.toList()));
    }

    public static Comment multiLineComment(String comment, String... more) {
        List<String> lines = new LinkedList<>();
        lines.add("/*");
        concat(comment, more)
                .stream()
                .map(" "::concat)
                .forEach(lines::add);
        lines.add(" */");
        return new Comment(lines);
    }

    public List<String> getLines() {
        return lines;
    }
}
