package io.clutter.model.clazz;

import io.clutter.model.annotation.AnnotationT;
import io.clutter.model.common.Trait;
import io.clutter.model.common.Visibility;
import io.clutter.model.ctor.Constructor;
import io.clutter.model.field.Field;
import io.clutter.model.method.Method;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.GenericType;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final public class Construct {

    public enum ConstructType {
        CLASS, INTERFACE
    }

    private final List<AnnotationT> annotations = new LinkedList<>();
    private final List<Field> fields = new LinkedList<>();
    private final List<Constructor> constructors = new LinkedList<>();
    private final List<Method> methods = new LinkedList<>();
    private final List<GenericType> genericTypes = new LinkedList<>();
    private final List<BoxedType> interfaces = new LinkedList<>();
    private final List<Trait> traits = new LinkedList<>();

    private ConstructType constructType;
    private Visibility visibility;
    private String packageName;
    private String name;
    private BoxedType parentClass;

    private Construct(
        ConstructType constructType,
        List<AnnotationT> annotations,
        Visibility visibility,
        List<Trait> traits,
        String name,
        String packageName,
        List<Field> fields,
        List<Constructor> constructors,
        List<Method> methods,
        List<GenericType> genericTypes
    ) {
        this.constructType = constructType;
        this.annotations.addAll(annotations);
        this.visibility = visibility;
        this.traits.addAll(traits);
        this.name = name;
        this.packageName = packageName;
        this.methods.addAll(methods);
        this.fields.addAll(fields);
        this.constructors.addAll(constructors);
        this.genericTypes.addAll(genericTypes);
    }

    public Construct() {
    }

    public static Construct copyOf(Construct construct) {
        return new Construct(
            construct.constructType,
            construct.annotations,
            construct.visibility,
            construct.traits,
            construct.name,
            construct.packageName,
            construct.fields,
            construct.constructors,
            construct.methods,
            construct.genericTypes
        );
    }

    public Construct setConstructType(ConstructType constructType) {
        this.constructType = constructType;
        return this;
    }

    public Construct setAnnotations(Consumer<List<AnnotationT>> mutation) {
        mutation.accept(annotations);
        return this;
    }

    public Construct setVisibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Construct setTraits(Consumer<List<Trait>> mutation) {
        mutation.accept(traits);
        return this;
    }

    public Construct setName(String name) {
        this.name = name;
        return this;
    }

    public Construct setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public Construct setFields(Consumer<List<Field>> mutation) {
        mutation.accept(fields);
        return this;
    }

    public Construct setConstructors(Consumer<List<Constructor>> mutation) {
        mutation.accept(constructors);
        return this;
    }

    public Construct setMethods(Consumer<List<Method>> mutation) {
        mutation.accept(methods);
        return this;
    }

    public Construct setGenericTypes(Consumer<List<GenericType>> mutation) {
        mutation.accept(genericTypes);
        return this;
    }

    public Construct setInterfaces(Consumer<List<BoxedType>> mutation) {
        mutation.accept(interfaces);
        return this;
    }

    public Construct setParentClass(BoxedType parentClass) {
        this.parentClass = parentClass;
        return this;
    }

    public ConstructType getConstructType() {
        return constructType;
    }

    public List<AnnotationT> getAnnotations() {
        return List.copyOf(annotations);
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public List<Trait> getTraits() {
        return List.copyOf(traits);
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFullyQualifiedName() {
        return getPackageName() + "." + getName();
    }

    public List<Field> getFields() {
        return List.copyOf(fields);
    }

    public List<Constructor> getConstructors() {
        return List.copyOf(constructors);
    }

    public List<Method> getMethods() {
        return List.copyOf(methods);
    }

    public List<GenericType> getGenericTypes() {
        return List.copyOf(genericTypes);
    }

    public List<BoxedType> getInterfaces() {
        return List.copyOf(interfaces);
    }

    public BoxedType getParentClass() {
        return parentClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Construct construct = (Construct) o;
        return annotations.equals(construct.annotations) &&
            fields.equals(construct.fields) &&
            constructors.equals(construct.constructors) &&
            methods.equals(construct.methods) &&
            genericTypes.equals(construct.genericTypes) &&
            interfaces.equals(construct.interfaces) &&
            traits.equals(construct.traits) &&
            constructType == construct.constructType &&
            visibility == construct.visibility &&
            packageName.equals(construct.packageName) &&
            name.equals(construct.name) &&
            Objects.equals(parentClass, construct.parentClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName, name);
    }

    @Override
    public String toString() {
        return "Construct{" +
            "annotations=" + annotations +
            ", fields=" + fields +
            ", constructors=" + constructors +
            ", methods=" + methods +
            ", genericTypes=" + genericTypes +
            ", interfaces=" + interfaces +
            ", traits=" + traits +
            ", constructType=" + constructType +
            ", visibility=" + visibility +
            ", packageName='" + packageName + '\'' +
            ", name='" + name + '\'' +
            ", parentClass=" + parentClass +
            '}';
    }
}