package io.clutter.javax.factory;

import io.clutter.model.type.GenericType;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.SimpleTypeVisitor7;

import static io.clutter.model.type.GenericType.ANY;

final class JxWildcardTypeVisitor extends SimpleTypeVisitor7<GenericType, Void> {

    @Override
    public GenericType visitWildcard(WildcardType w, Void nothing) {
        if (w.getExtendsBound() != null) {
            return ANY.extend(JxBoxedTypeAdapter.from(w.getExtendsBound()));
        } else if (w.getSuperBound() != null) {
            return ANY.subclass(JxBoxedTypeAdapter.from(w.getSuperBound()));
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
