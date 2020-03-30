package io.clutter.processor;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

final public class JavaFile {

    private final String fullyQualifiedName;
    private final List<String> body;

    public JavaFile(String fullyQualifiedName, List<String> body) {
        this.fullyQualifiedName = fullyQualifiedName;
        this.body = body;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public void writeTo(Writer writer) throws IOException {
        int lastIndex = body.size() - 1;
        for (int i = 0; i < lastIndex; i++) {
            writer.write(body.get(i));
            writer.write(System.lineSeparator());
        }
        writer.write(body.get(lastIndex));
    }

    public List<String> getLines() {
        return body;
    }
}
