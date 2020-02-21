package io.clutter.writer.classtype;

import io.clutter.writer.annotation.AnnotationType;
import io.clutter.writer.classtype.modifiers.ClassModifiers;
import io.clutter.writer.constructor.Constructor;
import io.clutter.writer.field.Field;
import io.clutter.writer.method.Method;

import java.util.*;

final public class ClassType {

    private final String fullQualifiedName;
    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<String> interfaces = new LinkedHashSet<>();
    private final LinkedHashSet<Constructor> constructors = new LinkedHashSet<>();
    private final LinkedHashSet<Field> fields = new LinkedHashSet<>();
    private final LinkedHashSet<Method> methods = new LinkedHashSet<>();

    private String parentClass;
    private ClassModifiers classModifiers;

    public ClassType(String fullQualifiedName) {
        this.fullQualifiedName = fullQualifiedName;
        this.classModifiers = ClassModifiers.PUBLIC;
    }

    public ClassType setParentClass(String parentClass) {
        this.parentClass = parentClass;
        return this;
    }

    public ClassType setInterfaces(String... interfaces) {
        this.interfaces.clear();
        Collections.addAll(this.interfaces, interfaces);
        return this;
    }

    public ClassType setAnnotations(AnnotationType... annotations) {
        this.annotations.clear();
        Collections.addAll(this.annotations, annotations);
        return this;
    }

    public ClassType setClassModifiers(ClassModifiers classModifiers) {
        this.classModifiers = classModifiers;
        return this;
    }

    public ClassType setConstructors(Constructor... constructors) {
        this.constructors.clear();
        Collections.addAll(this.constructors, constructors);
        return this;
    }

    public ClassType setFields(Field... fields) {
        this.fields.clear();
        Collections.addAll(this.fields, fields);
        return this;
    }

    public ClassType setMethods(Method... methods) {
        this.methods.clear();
        Collections.addAll(this.methods, methods);
        return this;
    }

    public String getFullQualifiedName() {
        return fullQualifiedName;
    }

    public Optional<String> getParentClass() {
        return Optional.ofNullable(parentClass);
    }

    public Set<String> getInterfaces() {
        return interfaces;
    }

    public List<AnnotationType> getAnnotations() {
        return annotations;
    }

    public ClassModifiers getClassModifiers() {
        return classModifiers;
    }

    public Set<Constructor> getConstructors() {
        return constructors;
    }

    public Set<Field> getFields() {
        return fields;
    }

    public Set<Method> getMethods() {
        return methods;
    }
}
