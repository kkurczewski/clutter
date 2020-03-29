package io.clutter.javax.factory;

import io.clutter.javax.factory.visitors.WildcardTypeVisitor;
import io.clutter.model.type.WildcardType;

import javax.lang.model.type.TypeMirror;

final public class WildcardTypeFactory {

    private static final WildcardTypeVisitor WILDCARD_TYPE_VISITOR = new WildcardTypeVisitor();

    public static WildcardType from(TypeMirror returnType) {
        return returnType.accept(WILDCARD_TYPE_VISITOR, null);
    }
}