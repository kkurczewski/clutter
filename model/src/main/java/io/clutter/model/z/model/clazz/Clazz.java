package io.clutter.model.z.model.clazz;

import io.clutter.model.annotation.AnnotationType;
import io.clutter.model.type.GenericType;
import io.clutter.model.z.collection.SafeList;
import io.clutter.model.z.model.common.Trait;
import io.clutter.model.z.model.common.Visibility;
import io.clutter.model.z.model.ctor.Constructor;
import io.clutter.model.z.model.field.Field;
import io.clutter.model.z.model.method.Method;

import java.util.List;
import java.util.function.Consumer;

public class Clazz extends ClazzFactory {

    private final SafeList<AnnotationType> annotations;
    private Visibility visibility;
    private final SafeList<Trait> traits;
    private String name;
    private final SafeList<Field> fields;
    private final SafeList<Constructor> constructors;
    private final SafeList<Method> methods;
    private final SafeList<GenericType> genericTypes;

    public Clazz(
            SafeList<AnnotationType> annotations,
            Visibility visibility,
            SafeList<Trait> traits,
            String name,
            SafeList<Field> fields,
            SafeList<Constructor> constructors,
            SafeList<Method> methods,
            SafeList<GenericType> genericTypes
    ) {
        this.annotations = annotations;
        this.visibility = visibility;
        this.traits = traits;
        this.name = name;
        this.methods = methods;
        this.fields = fields;
        this.constructors = constructors;
        this.genericTypes = genericTypes;
    }

    public static Clazz copyOf(Clazz clazz) {
        return new Clazz(
                clazz.annotations,
                clazz.visibility,
                clazz.traits,
                clazz.name,
                clazz.fields,
                clazz.constructors,
                clazz.methods,
                clazz.genericTypes
        );
    }

    public Clazz setAnnotations(Consumer<List<AnnotationType>> mutation) {
        annotations.modify(mutation);
        return this;
    }

    public Clazz setVisibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Clazz setTraits(Consumer<List<Trait>> mutation) {
        traits.modify(mutation);
        return this;
    }

    public Clazz setName(String name) {
        this.name = name;
        return this;
    }

    public Clazz setGenericTypes(Consumer<List<GenericType>> mutation) {
        genericTypes.modify(mutation);
        return this;
    }

    public List<AnnotationType> getAnnotations() {
        return annotations.getValues();
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public List<Trait> getTraits() {
        return traits.getValues();
    }

    public String getName() {
        return name;
    }

    public List<Field> getFields() {
        return fields.getValues();
    }

    public List<Constructor> getConstructors() {
        return constructors.getValues();
    }

    public List<Method> getMethods() {
        return methods.getValues();
    }

    public SafeList<GenericType> getGenericTypes() {
        return genericTypes;
    }

    public <T> T accept(ClazzVisitor<T> visitor) {
        return visitor.visit(this);
    }
}