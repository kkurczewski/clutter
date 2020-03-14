package io.clutter.javax.factory;

import io.clutter.model.type.BoxedType;
import io.clutter.model.type.ContainerType;
import io.clutter.model.type.DynamicType;
import io.clutter.model.type.Type;

import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor7;
import java.lang.reflect.Array;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.clutter.model.type.BoundedWildcardType.BoundaryType.EXTENDS;

final public class TypeFactory {

    public static final Void NOTHING = null;

    public static Type of(TypeMirror returnType) {
        return returnType.accept(new SimpleTypeVisitor7<>() {
            @Override
            public Type visitDeclared(DeclaredType declaredType, Void nothing) {
                Class<?> type;
                try {
                    type = Class.forName(declaredType.asElement().toString());
                } catch (ClassNotFoundException e) {
                    return DynamicType.of(declaredType.toString());
                }

                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                return typeArguments.isEmpty()
                        ? BoxedType.of(type)
                        : containerType(type, typeArguments
                        .stream()
                        .map(genericArgument -> genericArgument.accept(this, NOTHING))
                        .collect(Collectors.toList()));
            }

            @Override
            public Type visitPrimitive(PrimitiveType primitive, Void nothing) {
                Class<?> type = Stream.of(
                        short.class, int.class, long.class, float.class,
                        double.class, byte.class, char.class, boolean.class, void.class)
                        .filter(primitiveClass -> primitiveClass
                                .getSimpleName()
                                .equals(primitive.toString())
                        )
                        .findFirst()
                        .orElseThrow();

                return io.clutter.model.type.PrimitiveType.of(type);
            }

            @Override
            public Type visitArray(ArrayType array, Void nothing) {
                return Type.of(Array.newInstance(array.getComponentType()
                        .accept(this, NOTHING)
                        .getType(), 0)
                        .getClass()
                );
            }

            @Override
            public Type visitWildcard(WildcardType wildcardType, Void nothing) {
                String[] parts = wildcardType.toString().split(" ");
                var alias = io.clutter.model.type.WildcardType.alias(parts[0]);
                switch (parts.length) {
                    case 1:
                        return alias;
                    case 3:
                        Class<?> exactType;
                        try {
                            exactType = Class.forName(parts[2]);
                        } catch (ClassNotFoundException e) {
                            return DynamicType.of(parts[2]);
                        }
                        return EXTENDS.toString().equals(parts[1])
                                ? alias.extend(exactType)
                                : alias.subclass(exactType);
                    default:
                        return io.clutter.model.type.WildcardType.ANY;
                }
            }
        }, NOTHING);
    }

    @SuppressWarnings("all")
    private static Type containerType(Class<?> type, List<Type> genericValues) {
        return ContainerType.of(type, genericValues.toArray(BoxedType[]::new));
    }
}
