package io.clutter.model.value;

import io.clutter.model.annotation.AnnotationT;

public interface ValueVisitor<T> {

    T visit(StringValue stringValue);

    T visit(EnumValue enumValue);

    T visit(PrimitiveValue primitiveValue);

    T visit(ClassValue classValue);

    T visit(AnnotationT annotationValue);

    T visit(ListValue listValue);
}