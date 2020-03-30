package io.clutter.model.classtype;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.classtype.modifiers.ClassTrait;
import io.clutter.model.classtype.modifiers.ClassVisibility;
import io.clutter.model.constructor.Constructor;
import io.clutter.model.field.Field;
import io.clutter.model.method.Method;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.WildcardType;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Stream;

final public class ClassType {

    private final String fullyQualifiedName;
    private final String packageName;

    private final List<AnnotationType> annotations = new LinkedList<>();
    private final LinkedHashSet<BoxedType> interfaces = new LinkedHashSet<>();
    private final LinkedHashSet<Constructor> constructors = new LinkedHashSet<>();
    private final LinkedHashSet<Field> fields = new LinkedHashSet<>();
    private final LinkedHashSet<Method> methods = new LinkedHashSet<>();
    private final LinkedHashSet<ClassTrait> traits = new LinkedHashSet<>();
    private final LinkedHashSet<WildcardType> genericParameters = new LinkedHashSet<>();

    private BoxedType superclass;
    private ClassVisibility visibility;

    /**
     * Creates class with default public visibility
     */
    public ClassType(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
        this.packageName = fullyQualifiedName.substring(0, Math.max(0, fullyQualifiedName.lastIndexOf('.')));
        this.visibility = ClassVisibility.PUBLIC;
    }

    public ClassType setSuperclass(BoxedType superclass) {
        this.superclass = superclass;
        return this;
    }

    public ClassType setSuperclass(Class<?> superclass) {
        return setSuperclass(BoxedType.of(superclass));
    }

    public ClassType setInterfaces(BoxedType... interfaces) {
        this.traits.clear();
        Collections.addAll(this.interfaces, interfaces);
        return this;
    }

    public ClassType setInterfaces(Class<?>... interfaces) {
        return setInterfaces(Stream.of(interfaces)
                .map(BoxedType::of)
                .toArray(BoxedType[]::new));
    }

    public ClassType setAnnotations(AnnotationType... annotations) {
        this.annotations.clear();
        Collections.addAll(this.annotations, annotations);
        return this;
    }

    @SafeVarargs
    final public ClassType setAnnotations(Class<? extends Annotation>... annotations) {
        return setAnnotations(Stream.of(annotations)
                .map(AnnotationType::new)
                .toArray(AnnotationType[]::new));
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

    public ClassType setGenericParameters(WildcardType... genericParameters) {
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

    public Optional<BoxedType> getSuperclass() {
        return Optional.ofNullable(superclass);
    }

    public Set<BoxedType> getInterfaces() {
        return interfaces;
    }

    public ClassVisibility getVisibility() {
        return visibility;
    }

    public List<ClassTrait> getTraits() {
        return new ArrayList<>(traits);
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

    public Set<WildcardType> getGenericParameters() {
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
        ClassType classType = (ClassType) o;
        return fullyQualifiedName.equals(classType.fullyQualifiedName) &&
                packageName.equals(classType.packageName) &&
                annotations.equals(classType.annotations) &&
                interfaces.equals(classType.interfaces) &&
                constructors.equals(classType.constructors) &&
                fields.equals(classType.fields) &&
                methods.equals(classType.methods) &&
                traits.equals(classType.traits) &&
                genericParameters.equals(classType.genericParameters) &&
                Objects.equals(superclass, classType.superclass) &&
                visibility == classType.visibility;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullyQualifiedName);
    }

    @Override
    public String toString() {
        return "ClassType{" +
                "fullyQualifiedName='" + fullyQualifiedName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", annotations=" + annotations +
                ", interfaces=" + interfaces +
                ", constructors=" + constructors +
                ", fields=" + fields +
                ", methods=" + methods +
                ", traits=" + traits +
                ", genericParameters=" + genericParameters +
                ", superclass=" + superclass +
                ", visibility=" + visibility +
                '}';
    }
}
