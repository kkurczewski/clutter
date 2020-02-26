package io.clutter.writer.model.classtype;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.classtype.modifiers.ClassTrait;
import io.clutter.writer.model.classtype.modifiers.ClassVisibility;
import io.clutter.writer.model.constructor.Constructor;
import io.clutter.writer.model.field.Field;
import io.clutter.writer.model.method.Method;

import java.util.*;
import java.util.stream.Stream;

final public class ClassType {

    private final String fullQualifiedName;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<String> interfaces = new LinkedHashSet<>();
    private final LinkedHashSet<Constructor> constructors = new LinkedHashSet<>();
    private final LinkedHashSet<Field> fields = new LinkedHashSet<>();
    private final LinkedHashSet<Method> methods = new LinkedHashSet<>();
    private final LinkedHashSet<ClassTrait> traits = new LinkedHashSet<>();
    private String parentClass;
    private ClassVisibility visibility;

    public ClassType(String fullQualifiedName) {
        this.fullQualifiedName = fullQualifiedName;
        this.visibility = ClassVisibility.PUBLIC;
    }

    public ClassType setParentClass(Class<?> parentClass) {
        return setParentClass(parentClass.getCanonicalName());
    }

    public ClassType setParentClass(String parentClass) {
        this.parentClass = parentClass;
        return this;
    }

    public ClassType setInterfaces(Class<?>... interfaces) {
        return setInterfaces(Stream.of(interfaces).map(Class::getCanonicalName).toArray(String[]::new));
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

    public ClassType setVisibility(ClassVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public ClassType setTraits(ClassTrait... traits) {
        this.traits.clear();
        Collections.addAll(this.traits, traits);
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

    public ClassVisibility getVisibility() {
        return visibility;
    }

    public LinkedHashSet<ClassTrait> getTraits() {
        return traits;
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
