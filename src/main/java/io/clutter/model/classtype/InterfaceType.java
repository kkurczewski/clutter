package io.clutter.model.classtype;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.method.Method;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.WildcardType;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

final public class InterfaceType {

    private final String fullyQualifiedName;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<BoxedType> interfaces = new LinkedHashSet<>();
    private final LinkedHashSet<Method> methods = new LinkedHashSet<>();
    private final LinkedHashSet<WildcardType> wildcardTypes = new LinkedHashSet<>();

    public InterfaceType(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public InterfaceType setInterfaces(BoxedType... interfaces) {
        this.interfaces.clear();
        Collections.addAll(this.interfaces, interfaces);
        return this;
    }

    public InterfaceType setInterfaces(Class<?>... interfaces) {
        return setInterfaces(Stream.of(interfaces).map(BoxedType::of).toArray(BoxedType[]::new));
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

    public InterfaceType setWildcardTypes(WildcardType wildcardTypes) {
        this.wildcardTypes.clear();
        Collections.addAll(this.wildcardTypes, wildcardTypes);
        return this;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public Set<BoxedType> getInterfaces() {
        return interfaces;
    }

    public Set<Method> getMethods() {
        return methods;
    }

    public Set<WildcardType> getWildcardTypes() {
        return wildcardTypes;
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
        return fullyQualifiedName + wildcardTypes;
    }
}
