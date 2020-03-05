package io.clutter.writer.model.classtype;

import io.clutter.writer.model.annotation.AnnotationType;
import io.clutter.writer.model.method.Method;
import io.clutter.writer.model.type.Type;
import io.clutter.writer.model.type.WildcardType;
import io.clutter.writer.model.type.WrappedType;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

final public class InterfaceType {

    private final String fullyQualifiedName;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<String> interfaces = new LinkedHashSet<>();
    private final LinkedHashSet<Method> methods = new LinkedHashSet<>();
    private final LinkedHashSet<WildcardType> genericTypes = new LinkedHashSet<>();

    public InterfaceType(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public InterfaceType setInterfaces(String... interfaces) {
        this.interfaces.clear();
        Collections.addAll(this.interfaces, interfaces);
        return this;
    }

    public InterfaceType setInterfaces(Class<?>... interfaces) {
        return setInterfaces(Stream.of(interfaces).map(Class::getCanonicalName).toArray(String[]::new));
    }

    public InterfaceType setInterfaces(WrappedType... interfaces) {
        return setInterfaces(Stream.of(interfaces).map(Type::toString).toArray(String[]::new));
    }

    public InterfaceType setAnnotations(AnnotationType... annotations) {
        this.annotations.clear();
        Collections.addAll(this.annotations, annotations);
        return this;
    }

    @SafeVarargs
    final public InterfaceType setAnnotations(Class<? extends Annotation>... annotations) {
        return setAnnotations(Stream.of(annotations).map(AnnotationType::of).toArray(AnnotationType[]::new));
    }

    public InterfaceType setMethods(Method... methods) {
        this.methods.clear();
        Collections.addAll(this.methods, methods);
        return this;
    }

    public InterfaceType setGenericTypes(WildcardType genericTypes) {
        this.genericTypes.clear();
        Collections.addAll(this.genericTypes, genericTypes);
        return this;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public Set<String> getInterfaces() {
        return interfaces;
    }

    public Set<Method> getMethods() {
        return methods;
    }

    public Set<WildcardType> getGenericTypes() {
        return genericTypes;
    }

    public List<AnnotationType> getAnnotations() {
        return annotations;
    }

    public Optional<AnnotationType> getAnnotation(Class<? extends Annotation> annotation) throws NoSuchElementException {
        return annotations.stream()
                .filter(annotationType -> annotationType.isInstanceOf(annotation))
                .findFirst();
    }

    @SafeVarargs
    final public boolean isAnnotated(Class<? extends Annotation> annotation, Class<? extends Annotation>... more) {
        return getAnnotations().stream().anyMatch(a -> a.isInstanceOf(annotation, more));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterfaceType that = (InterfaceType) o;
        return fullyQualifiedName.equals(that.fullyQualifiedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullyQualifiedName);
    }

    @Override
    public String toString() {
        return fullyQualifiedName + genericTypes;
    }
}
