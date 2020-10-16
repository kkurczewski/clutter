package io.clutter.model.classtype;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.method.Method;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.GenericType;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

final public class InterfaceType {

    private final String fullyQualifiedName;
    private final String packageName;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<BoxedType> interfaces = new LinkedHashSet<>();
    private final LinkedHashSet<Method> methods = new LinkedHashSet<>();
    private final LinkedHashSet<GenericType> genericParameters = new LinkedHashSet<>();

    public InterfaceType(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
        this.packageName = fullyQualifiedName.substring(0, Math.max(0, fullyQualifiedName.lastIndexOf('.')));
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
        return setAnnotations(Stream.of(annotations)
                .map(AnnotationType::new)
                .toArray(AnnotationType[]::new));
    }

    public InterfaceType setMethods(Method... methods) {
        this.methods.clear();
        Collections.addAll(this.methods, methods);
        return this;
    }

    public InterfaceType setGenericParameters(GenericType genericParameters) {
        this.genericParameters.clear();
        Collections.addAll(this.genericParameters, genericParameters);
        return this;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public String getPackage() {
        return packageName;
    }

    public String getSimpleName() {
        return getFullyQualifiedName().substring(getPackage().length() + 1);
    }

    public Set<BoxedType> getInterfaces() {
        return interfaces;
    }

    public Set<Method> getMethods() {
        return methods;
    }

    public Set<GenericType> getGenericParameters() {
        return genericParameters;
    }

    public List<AnnotationType> getAnnotations() {
        return annotations;
    }

    public <T extends Annotation> Optional<T> getAnnotation(Class<T> annotation) {
        return annotations.stream()
                .filter(annotationType -> annotationType.isInstanceOf(annotation))
                .findFirst()
                .map(AnnotationType::reflect);
    }

    @SafeVarargs
    final public boolean isAnnotated(Class<? extends Annotation> annotation, Class<? extends Annotation>... more) {
        return getAnnotations().stream().anyMatch(it -> it.isInstanceOf(annotation, more));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterfaceType that = (InterfaceType) o;
        return fullyQualifiedName.equals(that.fullyQualifiedName) &&
                annotations.equals(that.annotations) &&
                interfaces.equals(that.interfaces) &&
                methods.equals(that.methods) &&
                genericParameters.equals(that.genericParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullyQualifiedName);
    }

    @Override
    public String toString() {
        return "InterfaceType{" +
                "fullyQualifiedName='" + fullyQualifiedName + '\'' +
                ", annotations=" + annotations +
                ", interfaces=" + interfaces +
                ", methods=" + methods +
                ", genericParameters=" + genericParameters +
                '}';
    }
}
