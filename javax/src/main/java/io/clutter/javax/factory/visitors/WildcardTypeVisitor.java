package io.clutter.javax.factory.visitors;

import io.clutter.javax.factory.types.BoxedTypeFactory;
import io.clutter.model.type.BoxedType;
import io.clutter.model.type.GenericType;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.SimpleTypeVisitor7;

import static io.clutter.model.type.GenericType.ANY;

final public class WildcardTypeVisitor extends SimpleTypeVisitor7<GenericType, Void> {

    @Override
    public GenericType visitWildcard(javax.lang.model.type.WildcardType w, Void nothing) {
        if (w.getExtendsBound() != null) {
            BoxedType boundary = BoxedTypeFactory.from(w.getExtendsBound());
            return ANY.extend(boundary);
        } else if (w.getSuperBound() != null) {
            BoxedType boundary = BoxedTypeFactory.from(w.getSuperBound());
            return ANY.subclass(boundary);
        } else return ANY;
    }

    @Override
    public GenericType visitTypeVariable(TypeVariable t, Void nothing) {
        return GenericType.alias(t.asElement().getSimpleName().toString());
    }

    @Override
    protected GenericType defaultAction(TypeMirror e, Void nothing) {
        throw new UnsupportedOperationException(e.toString());
    }
}
