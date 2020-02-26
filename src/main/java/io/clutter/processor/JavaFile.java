package io.clutter.processor;

import java.io.Writer;

public interface JavaFile {
    String getFullQualifiedName();

    void writeTo(Writer writer);
}
