package io.clutter.model.z.model.value.array;

public interface ValueArrayVisitor<T> {

    T visit(AnnotationArrayValue annotationValue);

    T visit(StringArrayValue stringValue);

    T visit(EnumArrayValue enumValue);

    T visit(PrimitiveArrayValue primitiveValue);

    T visit(ClassArrayValue classArrayValue);
}