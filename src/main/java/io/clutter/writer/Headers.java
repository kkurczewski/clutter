package io.clutter.writer;

import io.clutter.common.Varargs;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;

final public class Headers {

    private static final String NEW_LINE = "";

    private final String packageName;

    private final LinkedHashSet<String> imports = new LinkedHashSet<>();
    private Comments comments;

    private Headers(String packageName) {
        this.packageName = packageName;
    }

    public static Headers ofPackage(String packageName) {
        return new Headers(packageName);
    }

    public Headers addImport(Class<?> importType, Class<?>... more) {
        Varargs.concat(importType, more)
                .stream()
                .map(Class::getCanonicalName)
                .map(i -> format("import %s;", i))
                .forEach(imports::add);
        return this;
    }

    public Headers addStaticImport(Class<?> importType, Class<?>... more) {
        Varargs.concat(importType, more)
                .stream()
                .map(Class::getCanonicalName)
                .map(i -> format("import static %s;", i))
                .forEach(imports::add);
        return this;
    }

    public Headers addComments(Comments comments) {
        this.comments = comments;
        return this;
    }

    public List<String> asLines() {
        List<String> lines = new LinkedList<>();
        lines.add(format("package %s;", packageName));
        lines.add(NEW_LINE);
        if (!imports.isEmpty()) {
            lines.addAll(imports);
            lines.add(NEW_LINE);
        }
        if (comments != null) {
            lines.addAll(comments.getLines());
        }
        return lines;
    }
}
