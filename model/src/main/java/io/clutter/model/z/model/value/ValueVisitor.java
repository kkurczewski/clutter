package io.clutter.model.z.model.value;

public interface ValueVisitor<T> {

    T visit(StringValue stringValue);

    T visit(EnumValue enumValue);

    T visit(PrimitiveValue primitiveValue);

    T visit(ClassValue classValue);

    T visit(AnnotationValue annotationValue);
}