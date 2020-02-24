package io.clutter.writer.model.classtype;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.classtype.modifiers.ClassTrait;
import io.clutter.writer.model.classtype.modifiers.ClassVisibility;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.method.modifiers.MethodTrait;

import java.util.*;

// TODO adapt interface
final public class InterfaceType {

    private final String fullQualifiedName;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<String> interfaces = new LinkedHashSet<>();
    private final LinkedHashSet<Method> methods = new LinkedHashSet<>();
    private final LinkedHashSet<ClassTrait> traits = new LinkedHashSet<>();
    private String parentClass;
    private ClassVisibility visibility;

    public InterfaceType(String fullQualifiedName) {
        this.fullQualifiedName = fullQualifiedName;
        this.visibility = ClassVisibility.PUBLIC;
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

    public InterfaceType setVisibility(ClassVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public InterfaceType setTraits(ClassTrait... traits) {
        this.traits.clear();
        Collections.addAll(this.traits, traits);
        return this;
    }

    public InterfaceType setMethods(Method... methods) {
        this.methods.clear();
        // TODO handle redundant interface traits
        Arrays.asList(methods).forEach(method -> method.getTraits().add(MethodTrait.ABSTRACT));
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

    public Set<Method> getMethods() {
        return methods;
    }
}
