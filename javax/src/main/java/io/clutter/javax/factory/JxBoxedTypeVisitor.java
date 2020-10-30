package io.clutter.javax.factory;

import io.clutter.model.type.BoxedType;
import io.clutter.model.type.ContainerType;

import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor7;
import java.lang.reflect.Array;

final class JxBoxedTypeVisitor extends SimpleTypeVisitor7<BoxedType, Void> {

    private final JxWildcardTypeVisitor jxWildcardTypeVisitor = new JxWildcardTypeVisitor();

    @Override
    public BoxedType visitDeclared(DeclaredType d, Void nothing) {
        Class<?> type;
        try {
            type = Class.forName(d.asElement().accept(new JxNestedClassVisitor(), false));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        var typeArguments = d.getTypeArguments();
        var genericArguments = typeArguments
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
        return jxWildcardTypeVisitor.visitWildcard(w, nothing);
    }

    @Override
    public BoxedType visitTypeVariable(TypeVariable t, Void nothing) {
        return jxWildcardTypeVisitor.visitTypeVariable(t, nothing);
    }

    @Override
    protected BoxedType defaultAction(TypeMirror e, Void nothing) {
        if (e.getKind() == TypeKind.ERROR) {
            throw new IllegalArgumentException("Could not resolve: " + e.toString());
        }
        throw new UnsupportedOperationException("Type not supported: " + e.getKind().toString());
    }
}