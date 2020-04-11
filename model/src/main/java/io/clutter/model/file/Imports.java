package io.clutter.model.file;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

final public class Imports {

    private Set<Class<?>> imports = new HashSet<>();
    private Set<Class<?>> staticImports = new HashSet<>();

    /**
     * Returns true if no name collision occurred
     */
    public boolean addImport(Class<?> clazz) {
        boolean noCollision = imports
                .stream()
                .map(Class::getSimpleName)
                .noneMatch(clazz.getName()::equals);
        if (noCollision) {
            imports.add(clazz);
        }
        return noCollision;
    }

    /**
     * Returns true if no name collision occurred
     */
    public boolean addStaticImport(Class<?> clazz) {
        boolean noCollision = staticImports
                .stream()
                .map(Class::getSimpleName)
                .noneMatch(clazz.getName()::equals);
        if (noCollision) {
            staticImports.add(clazz);
        }
        return noCollision;
    }

    public List<String> getLines() {
        List<String> lines = new LinkedList<>(imports());
        lines.addAll(staticImports());
        return lines;
    }

    private Set<String> imports() {
        return imports.stream()
                .map(Class::getCanonicalName)
                .map(clazz -> format("import %s;", clazz))
                .collect(Collectors.toSet());
    }

    private Set<String> staticImports() {
        return staticImports.stream()
                .map(Class::getCanonicalName)
                .map(clazz -> format("import static %s;", clazz))
                .collect(Collectors.toSet());
    }

    public boolean contains(Class<?> clazz) {
        return imports.contains(clazz) || staticImports.contains(clazz);
    }
}
