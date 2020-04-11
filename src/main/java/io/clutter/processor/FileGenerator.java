package io.clutter.processor;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.io.UncheckedIOException;

final public class FileGenerator {

    private final Filer filer;

    FileGenerator(Filer filer) {
        this.filer = filer;
    }

    public void writeSourceFile(JavaFile javaFile) {
        try (var writer = filer.createSourceFile(javaFile.getFullyQualifiedName()).openWriter()) {
            javaFile.writeTo(writer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
