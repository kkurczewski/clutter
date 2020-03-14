package io.clutter.processor;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface JavaFile {

    String getFullQualifiedName();

    List<String> getLines();

    default void writeTo(Writer writer) {
        getLines().forEach(line -> {
            try {
                writer.write(line);
                writer.write(System.lineSeparator());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
