package io.clutter.model.file;

import io.clutter.model.classtype.ClassType;
import io.clutter.printer.AutoImportingTypePrinter;
import io.clutter.printer.ClassPrinter;

import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

final public class JavaFileBuilder {

    private final String packageName;
    private final String fullyQualifiedName;
    private final Imports imports;
    private final List<String> body;

    private Comment comment;

    private JavaFileBuilder(String packageName, String fullyQualifiedName, Imports imports, List<String> body) {
        this.packageName = requireNonNull(packageName);
        this.fullyQualifiedName = requireNonNull(fullyQualifiedName);
        this.imports = requireNonNull(imports);
        this.body = requireNonNull(body);
    }

    public static JavaFileBuilder from(ClassType classType) {
        var typePrinter = new AutoImportingTypePrinter();
        var classPrinter = new ClassPrinter(typePrinter);
        var body = classPrinter.print(classType);

        return new JavaFileBuilder(
                classType.getPackage(),
                classType.getFullyQualifiedName(),
                typePrinter.getImports(),
                body
        );
    }

    public JavaFileBuilder addImport(Class<?> clazz) {
        this.imports.addImport(clazz);
        return this;
    }

    public JavaFileBuilder addStaticImport(Class<?> clazz) {
        this.imports.addStaticImport(clazz);
        return this;
    }

    public JavaFileBuilder setComment(Comment comment) {
        this.comment = comment;
        return this;
    }

    public JavaFileBuilder appendBody(List<String> body) {
        this.body.addAll(body);
        return this;
    }

    public JavaFile build() {
        List<String> lines = new LinkedList<>();
        lines.add(format("package %s;", packageName));
        List<String> importsLines = imports.getLines();
        if (!importsLines.isEmpty()) {
            lines.add("");
            lines.addAll(importsLines);
        }
        if (comment != null && !comment.getLines().isEmpty()) {
            lines.add("");
            lines.addAll(comment.getLines());
        }
        if (!body.isEmpty()) {
            lines.add("");
            lines.addAll(body);
        }
        return new JavaFile(fullyQualifiedName, lines);
    }
}