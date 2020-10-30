package io.clutter.javax.factory;

import io.clutter.model.type.PrimitiveType;
import io.clutter.model.type.Type;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor7;
import java.lang.reflect.Array;
import java.util.Set;

import static io.clutter.model.type.PrimitiveType.VOID;

final class JxTypeVisitor extends SimpleTypeVisitor7<Type, Void> {

    private final Set<Class<?>> primitives = Set.of(
        short.class, int.class, long.class, float.class, double.class,
        byte.class, char.class, boolean.class, void.class
    );
    private final JxBoxedTypeVisitor jxBoxedTypeVisitor = new JxBoxedTypeVisitor();

    @Override
    public Type visitPrimitive(javax.lang.model.type.PrimitiveType p, Void nothing) {
        return primitives
            .stream()
            .filter(clazz -> clazz.getSimpleName().equals(p.toString()))
            .findFirst()
            .map(PrimitiveType::of)
            .orElseThrow();
    }

    @Override
    public Type visitNoType(NoType t, Void nothing) {
        return VOID;
    }

    @Override
    public Type visitArray(ArrayType a, Void nothing) {
        return Type.of(Array.newInstance(a.getComponentType()
            .accept(this, null)
            .getType(), 0)
            .getClass()
        );
    }

    @Override
    protected Type defaultAction(TypeMirror e, Void nothing) {
        return e.accept(jxBoxedTypeVisitor, null);
    }
}