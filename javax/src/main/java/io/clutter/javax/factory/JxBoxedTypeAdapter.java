package io.clutter.javax.factory;

import io.clutter.model.type.BoxedType;

import javax.lang.model.type.TypeMirror;

final public class JxBoxedTypeAdapter {

    private static final JxBoxedTypeVisitor BOXED_TYPE_VISITOR = new JxBoxedTypeVisitor();

    public static BoxedType from(TypeMirror returnType) {
        return returnType.accept(BOXED_TYPE_VISITOR, null);
    }
}