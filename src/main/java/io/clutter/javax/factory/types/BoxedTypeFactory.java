package io.clutter.javax.factory.types;

import io.clutter.javax.factory.visitors.BoxedTypeVisitor;
import io.clutter.model.type.BoxedType;

import javax.lang.model.type.TypeMirror;

final public class BoxedTypeFactory {

    private static final BoxedTypeVisitor BOXED_TYPE_VISITOR = new BoxedTypeVisitor();

    public static BoxedType from(TypeMirror returnType) {
        return returnType.accept(BOXED_TYPE_VISITOR, null);
    }
}