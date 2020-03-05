package io.clutter.writer.expressions;

import io.clutter.writer.model.type.Type;
import io.clutter.writer.model.type.WrappedType;

import java.util.*;

import static java.lang.String.format;

final public class Collections {

    public static String newArrayList() {
        return newCollection(ArrayList.class);
    }

    public static String newArrayList(Type type) {
        return newCollection(ArrayList.class, type);
    }

    public static String newHashSet() {
        return newCollection(HashSet.class);
    }

    public static String newHashSet(Type type) {
        return newCollection(HashSet.class, type);
    }

    public static String newHashMap() {
        return newMap(HashMap.class);
    }

    public static String newHashMap(Type keyType, Type valueType) {
        return newMap(HashMap.class, keyType, valueType);
    }

    @SuppressWarnings("rawtypes")
    public static String newCollection(Class<? extends Collection> type) {
        return format("new %s()", WrappedType.generic(type));
    }

    @SuppressWarnings("rawtypes")
    public static String newCollection(Class<? extends Collection> type, Type elementType) {
        return format("new %s()", WrappedType.generic(type, elementType));
    }

    @SuppressWarnings("rawtypes")
    public static String newMap(Class<? extends Map> type) {
        return format("new %s()", WrappedType.generic(type));
    }

    @SuppressWarnings("rawtypes")
    public static String newMap(Class<? extends Map> type, Type keyType, Type valueType) {
        return format("new %s()", WrappedType.generic(type, keyType, valueType));
    }

}
