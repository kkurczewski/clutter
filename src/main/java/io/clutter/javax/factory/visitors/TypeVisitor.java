package io.clutter.javax.factory.visitors;

import io.clutter.model.type.PrimitiveType;
import io.clutter.model.type.Type;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor7;
import java.lang.reflect.Array;

import static io.clutter.javax.factory.common.PrimitiveUtils.primitives;

final public class TypeVisitor extends SimpleTypeVisitor7<Type, Void> {

    private final BoxedTypeVisitor boxedTypeVisitor = new BoxedTypeVisitor();

    @Override
    public Type visitPrimitive(javax.lang.model.type.PrimitiveType p, Void nothing) {
        Class<?> type = primitives()
                .stream()
                .filter(clazz -> clazz.getSimpleName().equals(p.toString()))
                .findFirst()
                .orElseThrow();

        return PrimitiveType.of(type);
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
        return e.accept(boxedTypeVisitor, null);
    }
}