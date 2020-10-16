package io.clutter.javax.factory.types;

import io.clutter.javax.factory.visitors.WildcardTypeVisitor;
import io.clutter.model.type.GenericType;

import javax.lang.model.element.Element;
import javax.lang.model.element.Parameterizable;
import javax.lang.model.type.TypeMirror;

final public class WildcardTypeFactory {

    private static final WildcardTypeVisitor WILDCARD_TYPE_VISITOR = new WildcardTypeVisitor();

    public static GenericType from(TypeMirror returnType) {
        return returnType.accept(WILDCARD_TYPE_VISITOR, null);
    }

    public static GenericType[] from(Parameterizable parameterizable) {
        return parameterizable
                .getTypeParameters()
                .stream()
                .map(Element::asType)
                .map(WildcardTypeFactory::from)
                .toArray(GenericType[]::new);
    }
}