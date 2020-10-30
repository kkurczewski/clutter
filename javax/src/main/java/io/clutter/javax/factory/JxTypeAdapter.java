package io.clutter.javax.factory;

import io.clutter.model.type.Type;

import javax.lang.model.type.TypeMirror;

final public class JxTypeAdapter {

    private static final JxTypeVisitor TYPE_VISITOR = new JxTypeVisitor();

    public static Type from(TypeMirror returnType) {
        return returnType.accept(TYPE_VISITOR, null);
    }
}