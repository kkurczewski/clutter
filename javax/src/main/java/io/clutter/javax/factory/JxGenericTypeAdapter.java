package io.clutter.javax.factory;

import io.clutter.model.type.GenericType;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static java.util.stream.Collectors.toList;

final public class JxGenericTypeAdapter {

    private static final JxWildcardTypeVisitor WILDCARD_TYPE_VISITOR = new JxWildcardTypeVisitor();

    public static GenericType from(TypeMirror returnType) {
        return returnType.accept(WILDCARD_TYPE_VISITOR, null);
    }

    public static List<GenericType> from(List<? extends TypeParameterElement> typeParameters) {
        return typeParameters
                .stream()
                .map(Element::asType)
                .map(JxGenericTypeAdapter::from)
                .collect(toList());
    }
}