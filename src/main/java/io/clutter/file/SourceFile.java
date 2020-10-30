package io.clutter.file;

import io.clutter.processor.SourceFileGenerator;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * @see SourceFileGenerator
 */
final public class SourceFile {

    private final String className;
    private final List<String> content;

    public SourceFile(String packageName, String className, List<String> imports, List<String> body) {
        var content = new LinkedList<String>();

        content.add(format("package %s;", packageName));
        if (!imports.isEmpty()) {
            content.add("");
            content.addAll(imports);
        }
        if (!body.isEmpty()) {
            content.add("");
            content.addAll(body);
        }
        this.className = requireNonNull(className);
        this.content = requireNonNull(content);
    }

    public String getClassName() {
        return className;
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
