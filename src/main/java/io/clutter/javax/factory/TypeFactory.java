package io.clutter.javax.factory;

import io.clutter.javax.factory.visitors.TypeVisitor;
import io.clutter.model.type.Type;

import javax.lang.model.type.TypeMirror;

final public class TypeFactory {

    private static final TypeVisitor TYPE_VISITOR = new TypeVisitor();

    public static Type from(TypeMirror returnType) {
        return returnType.accept(TYPE_VISITOR, null);
    }
}