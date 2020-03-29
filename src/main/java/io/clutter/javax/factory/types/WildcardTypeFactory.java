package io.clutter.javax.factory.types;

import io.clutter.javax.factory.visitors.WildcardTypeVisitor;
import io.clutter.model.type.WildcardType;

import javax.lang.model.element.Element;
import javax.lang.model.element.Parameterizable;
import javax.lang.model.type.TypeMirror;

final public class WildcardTypeFactory {

    private static final WildcardTypeVisitor WILDCARD_TYPE_VISITOR = new WildcardTypeVisitor();

    public static WildcardType from(TypeMirror returnType) {
        return returnType.accept(WILDCARD_TYPE_VISITOR, null);
    }

    public static WildcardType[] from(Parameterizable parameterizable) {
        return parameterizable
                .getTypeParameters()
                .stream()
                .map(Element::asType)
                .map(WildcardTypeFactory::from)
                .toArray(WildcardType[]::new);
    }
}