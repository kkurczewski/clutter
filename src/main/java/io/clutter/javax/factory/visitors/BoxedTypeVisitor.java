package io.clutter.javax.factory.visitors;

import io.clutter.model.type.BoxedType;
import io.clutter.model.type.ContainerType;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.SimpleTypeVisitor7;
import java.lang.reflect.Array;
import java.util.List;

final public class BoxedTypeVisitor extends SimpleTypeVisitor7<BoxedType, Void> {

    private final WildcardTypeVisitor wildcardTypeVisitor = new WildcardTypeVisitor();

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
        return wildcardTypeVisitor.visitWildcard(w, nothing);
    }

    @Override
    public BoxedType visitTypeVariable(TypeVariable t, Void nothing) {
        return wildcardTypeVisitor.visitTypeVariable(t, nothing);
    }

    @Override
    protected BoxedType defaultAction(TypeMirror e, Void nothing) {
        throw new UnsupportedOperationException(e.getKind().toString());
    }
}