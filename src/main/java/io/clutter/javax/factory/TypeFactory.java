package io.clutter.javax.factory;

import io.clutter.model.type.Type;

import javax.lang.model.type.TypeMirror;

final public class TypeFactory {
    public static Type of(TypeMirror returnType) {
        return Type.raw(String.valueOf(returnType));
    }
}
