package io.clutter.processor;

import io.clutter.file.JavaFile;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Allows to generate new classes before compilation step occurs.
 * This class will be provided on runtime during annotation processing phase.
 *
 * @see SimpleProcessor
 * @see JavaFile
 */
final public class FileGenerator {

    private final Filer filer;

    FileGenerator(Filer filer) {
        this.filer = filer;
    }

    /**
     * @throws UncheckedIOException if the file cannot be created
     */
    public void writeSourceFile(JavaFile javaFile) {
        try (var writer = filer.createSourceFile(javaFile.getFullyQualifiedName()).openWriter()) {
            javaFile.writeTo(writer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
