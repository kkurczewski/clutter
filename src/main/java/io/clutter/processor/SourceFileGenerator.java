package io.clutter.processor;

import io.clutter.file.SourceFile;

import javax.annotation.processing.Filer;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Allows to generate new classes before compilation step occurs.
 * This class will be provided on runtime during annotation processing phase.
 *
 * @see SimpleProcessor
 * @see SourceFile
 */
final public class SourceFileGenerator {

    private final Filer filer;

    SourceFileGenerator(Filer filer) {
        this.filer = filer;
    }

    /**
     * @throws UncheckedIOException if the file cannot be created
     */
    public void generateFile(SourceFile sourceFile) {
        try (var writer = filer.createSourceFile(sourceFile.getClassName()).openWriter()) {
            sourceFile.writeTo(writer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
