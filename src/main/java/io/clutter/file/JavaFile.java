package io.clutter.file;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * @see JavaFileFactory
 * @see io.clutter.processor.FileGenerator
 */
final public class JavaFile {

    private final String fullyQualifiedName;
    private final List<String> content;

    public JavaFile(String packageName, String fullyQualifiedName, List<String> imports, List<String> body) {
        List<String> content = new LinkedList<>();

        content.add(format("package %s;", packageName));
        if (!imports.isEmpty()) {
            content.add("");
            content.addAll(imports);
        }
        if (!body.isEmpty()) {
            content.add("");
            content.addAll(body);
        }
        this.fullyQualifiedName = requireNonNull(fullyQualifiedName);
        this.content = requireNonNull(content);
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public void writeTo(Writer writer) throws IOException {
        int lastIndex = content.size() - 1;
        for (int i = 0; i < lastIndex; i++) {
            writer.write(content.get(i));
            writer.write(System.lineSeparator());
        }
        writer.write(content.get(lastIndex));
    }

    public List<String> getLines() {
        return content;
    }
}
