package io.clutter.model.type;

public class BoxedType extends Type {

    public static final BoxedType STRING = new BoxedType(String.class);

    BoxedType(Class<?> type) {
        super(type);
    }

    public static BoxedType of(Class<?> type) {
        return type.isPrimitive() ? PrimitiveType.of(type).boxed() : new BoxedType(type);
    }
}
