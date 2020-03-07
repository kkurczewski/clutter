package io.clutter.model.classtype;

import io.clutter.model.classtype.modifiers.ClassTrait;
import io.clutter.model.classtype.modifiers.ClassVisibility;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.field.Field;
import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.method.Method;
import io.clutter.model.type.Type;
import io.clutter.model.type.WildcardType;
import io.clutter.model.type.WrappedType;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

final public class ClassType {

    private final String fullyQualifiedName;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<String> interfaces = new LinkedHashSet<>();
    private final LinkedHashSet<Constructor> constructors = new LinkedHashSet<>();
    private final LinkedHashSet<Field> fields = new LinkedHashSet<>();
    private final LinkedHashSet<Method> methods = new LinkedHashSet<>();
    private final LinkedHashSet<ClassTrait> traits = new LinkedHashSet<>();
    private final LinkedHashSet<WildcardType> genericTypes = new LinkedHashSet<>();
    private String parentClass;
    private ClassVisibility visibility;

    public ClassType(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
        this.visibility = ClassVisibility.PUBLIC;
    }

    public ClassType setParentClass(Class<?> parentClass) {
        return setParentClass(parentClass.getCanonicalName());
    }

    public ClassType setParentClass(WrappedType parentClass) {
        return setParentClass(parentClass.toString());
    }

    public ClassType setParentClass(String parentClass) {
        this.parentClass = parentClass;
        return this;
    }

    public ClassType setInterfaces(Class<?>... interfaces) {
        return setInterfaces(Stream.of(interfaces).map(Class::getCanonicalName).toArray(String[]::new));
    }

    public ClassType setInterfaces(WrappedType... interfaces) {
        return setInterfaces(Stream.of(interfaces).map(Type::toString).toArray(String[]::new));
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

    @SafeVarargs
    final public ClassType setAnnotations(Class<? extends Annotation>... annotations) {
        return setAnnotations(Stream.of(annotations).map(AnnotationType::of).toArray(AnnotationType[]::new));
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

    public ClassType setGenericTypes(WildcardType... genericTypes) {
        this.genericTypes.clear();
        Collections.addAll(this.genericTypes, genericTypes);
        return this;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public Optional<String> getParentClass() {
        return Optional.ofNullable(parentClass);
    }

    public Set<String> getInterfaces() {
        return interfaces;
    }

    public ClassVisibility getVisibility() {
        return visibility;
    }

    public Set<ClassTrait> getTraits() {
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
        ClassType classType = (ClassType) o;
        return fullyQualifiedName.equals(classType.fullyQualifiedName);
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
