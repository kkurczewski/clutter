package io.clutter.processor;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.io.UncheckedIOException;

final public class FileGenerator {

    private final Filer filer;

    public FileGenerator(Filer filer) {
        this.filer = filer;
    }

    /**
     * @throws UncheckedIOException when IO error occurs
     */
    public void createSourceFile(JavaFile javaFile) {
        try (var writer = filer.createSourceFile(javaFile.getFullQualifiedName()).openWriter()) {
            javaFile.writeTo(writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
