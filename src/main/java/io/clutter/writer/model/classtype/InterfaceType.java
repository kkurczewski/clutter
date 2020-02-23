package io.clutter.writer.model.classtype;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.classtype.modifiers.ClassModifiers;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.method.modifiers.MethodModifiers;
import io.clutter.writer.model.method.modifiers.MethodTrait;

import java.util.*;

// TODO adapt interface
final public class InterfaceType {

    private final String fullQualifiedName;
    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<String> interfaces = new LinkedHashSet<>();
    private final LinkedHashSet<Method> methods = new LinkedHashSet<>();

    private String parentClass;
    private ClassModifiers classModifiers;

    public InterfaceType(String fullQualifiedName) {
        this.fullQualifiedName = fullQualifiedName;
        this.classModifiers = ClassModifiers.PUBLIC;
    }

    public InterfaceType setParentClass(String parentClass) {
        this.parentClass = parentClass;
        return this;
    }

    public InterfaceType setInterfaces(String... interfaces) {
        this.interfaces.clear();
        Collections.addAll(this.interfaces, interfaces);
        return this;
    }

    public InterfaceType setAnnotations(AnnotationType... annotations) {
        this.annotations.clear();
        Collections.addAll(this.annotations, annotations);
        return this;
    }

    public InterfaceType setClassModifiers(ClassModifiers classModifiers) {
        this.classModifiers = classModifiers;
        return this;
    }

    public InterfaceType setMethods(Method... methods) {
        this.methods.clear();
        // TODO handle redundant interface traits
        Arrays.asList(methods).forEach(method -> method.getModifiers().addTrait(MethodTrait.ABSTRACT));
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

    public Set<Method> getMethods() {
        return methods;
    }
}
