package io.clutter;

import com.google.testing.compile.JavaFileObjects;

import javax.tools.JavaFileObject;
import java.lang.annotation.Annotation;
import java.util.*;

import static java.lang.String.format;

public class SimpleClassBuilder {

    private final String fullyQualifiedName;
    private final Set<Class<? extends Annotation>> annotations;
    private final Map<String, Set<Class<? extends Annotation>>> methods = new HashMap<>();
    private final Map<String, Set<Class<? extends Annotation>>> fields = new HashMap<>();

    @SafeVarargs
    private SimpleClassBuilder(String fullyQualifiedName, Class<? extends Annotation>... annotations) {
        this.fullyQualifiedName = fullyQualifiedName;
        this.annotations = Set.of(annotations);
    }

    @SafeVarargs
    public static SimpleClassBuilder newClass(String fullyQualifiedName, Class<? extends Annotation>... annotations) {
        return new SimpleClassBuilder(fullyQualifiedName, annotations);
    }

    @SafeVarargs
    final public SimpleClassBuilder addMethod(String name, Class<? extends Annotation>... annotations) {
        methods.put(name, Set.of(annotations));
        return this;
    }

    @SafeVarargs
    final public SimpleClassBuilder addField(String name, Class<? extends Annotation>... annotations) {
        fields.put(name, Set.of(annotations));
        return this;
    }

    public JavaFileObject build() {
        int lastDotSeparator = fullyQualifiedName.lastIndexOf(".");
        String[] nameParts = {
                fullyQualifiedName.substring(0, lastDotSeparator),
                fullyQualifiedName.substring(lastDotSeparator + 1)
        };
        List<String> fileLines = new ArrayList<>();
        fileLines.add(format("package %s;", nameParts[0]));
        annotations.forEach(annotation -> fileLines.add(format("@%s", annotation.getCanonicalName())));
        fileLines.add(format("public class %s {", nameParts[1]));
        fields.forEach((key, value) -> {
            value.forEach(annotation -> fileLines.add(format("@%s", annotation.getCanonicalName())));
            fileLines.add(format("private String %s;", key));
        });
        methods.forEach((key, value) -> {
            value.forEach(annotation -> fileLines.add(format("@%s", annotation.getCanonicalName())));
            fileLines.add(format("private void %s() {}", key));
        });
        fileLines.add("}");

        return JavaFileObjects.forSourceLines(fullyQualifiedName, fileLines);
    }
}
