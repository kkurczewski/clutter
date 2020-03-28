package io.clutter.javax.factory.visitors;

import io.clutter.model.type.BoxedType;
import io.clutter.model.type.ContainerType;
import io.clutter.model.type.WildcardType;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.SimpleTypeVisitor7;
import java.lang.reflect.Array;
import java.util.List;

import static io.clutter.model.type.WildcardType.ANY;

final public class BoxedTypeVisitor extends SimpleTypeVisitor7<BoxedType, Void> {

    @Override
    public BoxedType visitDeclared(DeclaredType d, Void nothing) {
        Class<?> type;
        try {
            type = Class.forName(d.asElement().accept(new NestedClassVisitor(), false));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<? extends TypeMirror> typeArguments = d.getTypeArguments();
        BoxedType[] genericArguments = typeArguments
                .stream()
                .map(genericArgument -> genericArgument.accept(this, null))
                .toArray(BoxedType[]::new);
        return typeArguments.isEmpty()
                ? BoxedType.of(type)
                : ContainerType.genericOf(type, genericArguments);
    }

    @Override
    public BoxedType visitArray(ArrayType a, Void nothing) {
        return BoxedType.of(Array.newInstance(a.getComponentType()
                .accept(this, null)
                .getType(), 0)
                .getClass()
        );
    }

    @Override
    public BoxedType visitWildcard(javax.lang.model.type.WildcardType w, Void nothing) {
        if (w.getExtendsBound() != null) {
            BoxedType boundary = w.getExtendsBound().accept(this, null);
            return ANY.extend(boundary);
        } else if (w.getSuperBound() != null){
            BoxedType boundary = w.getSuperBound().accept(this, null);
            return ANY.subclass(boundary);
        } else return ANY;
    }

    @Override
    public BoxedType visitTypeVariable(TypeVariable t, Void nothing) {
        return WildcardType.alias(t.asElement().getSimpleName().toString());
    }

    @Override
    protected BoxedType defaultAction(TypeMirror e, Void nothing) {
        throw new UnsupportedOperationException(e.getKind().toString());
    }
}