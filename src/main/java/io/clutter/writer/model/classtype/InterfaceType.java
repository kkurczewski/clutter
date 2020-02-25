package io.clutter.writer.model.classtype;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.method.modifiers.MethodTrait;

import java.util.*;

final public class InterfaceType {

    private final String fullQualifiedName;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<String> interfaces = new LinkedHashSet<>();
    private final LinkedHashSet<Method> methods = new LinkedHashSet<>();

    public InterfaceType(String fullQualifiedName) {
        this.fullQualifiedName = fullQualifiedName;
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

    public InterfaceType setMethods(Method... methods) {
        this.methods.clear();
        Arrays.asList(methods).forEach(method -> method.getTraits().add(MethodTrait.INTERFACE_ABSTRACT));
        Collections.addAll(this.methods, methods);
        return this;
    }

    public String getFullQualifiedName() {
        return fullQualifiedName;
    }

    public Set<String> getInterfaces() {
        return interfaces;
    }

    public List<AnnotationType> getAnnotations() {
        return annotations;
    }

    public Set<Method> getMethods() {
        return methods;
    }
}
