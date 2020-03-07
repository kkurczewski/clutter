package io.clutter.writer.model.type;

public class BasicTypes {

    public static final Type BYTE = primitiveType(byte.class, Byte.class);
    public static final Type SHORT = primitiveType(short.class, Short.class);
    public static final Type FLOAT = primitiveType(float.class, Float.class);
    public static final Type DOUBLE = primitiveType(double.class, Double.class);
    public static final Type CHAR = primitiveType(char.class, Character.class);
    public static final Type INT = primitiveType(int.class, Integer.class);
    public static final Type LONG = primitiveType(long.class, Long.class);
    public static final Type BOOLEAN = primitiveType(boolean.class, Boolean.class);
    public static final Type VOID = primitiveType(void.class, Void.class);
    public static final Type STRING = primitiveType(String.class, String.class);

    private static Type primitiveType(Class<?> value, Class<?> boxed) {
        return new Type(value.getSimpleName(), boxed.getSimpleName());
    }

}
