package io.clutter.file;

import io.clutter.model.classtype.ClassType;
import io.clutter.model.file.Comment;
import io.clutter.model.file.Imports;
import io.clutter.printer.AutoImportingTypePrinter;
import io.clutter.printer.ClassPrinter;
import io.clutter.printer.TypePrinter;

import java.util.List;

/**
 * @see JavaFile
 * @see io.clutter.processor.FileGenerator
 */
public class JavaFileFactory {

    private final Comment autoGeneratedComment;

    public JavaFileFactory() {
        this.autoGeneratedComment = null;
    }

    public JavaFileFactory(Comment autoGeneratedComment) {
        this.autoGeneratedComment = autoGeneratedComment;
    }

    public JavaFile withFixedImports(ClassType classType, Imports imports) {
        var typePrinter = new TypePrinter(imports);
        return new JavaFile(
                classType.getPackage(),
                classType.getFullyQualifiedName(),
                preProcessImports(typePrinter.getImports().getLines()),
                preProcessBody(new ClassPrinter(typePrinter).print(classType))
        );
    }

    public JavaFile withoutImports(ClassType classType) {
        return withFixedImports(classType, new Imports());
    }

    public JavaFile withAutoImports(ClassType classType) {
        var typePrinter = new AutoImportingTypePrinter();
        return new JavaFile(
                classType.getPackage(),
                classType.getFullyQualifiedName(),
                preProcessImports(typePrinter.getImports().getLines()),
                preProcessBody(new ClassPrinter(typePrinter).print(classType))
        );
    }

    protected List<String> preProcessImports(List<String> imports) {
        return imports;
    }

    protected List<String> preProcessBody(List<String> body) {
        if (autoGeneratedComment != null) {
            List<String> commentLines = autoGeneratedComment.getLines();
            commentLines.addAll(body);
            return commentLines;
        }
        return body;
    }
}