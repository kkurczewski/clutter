package io.clutter;

import io.clutter.model.annotation.AnnotationT;
import io.clutter.model.value.ClassValue;
import io.clutter.model.value.*;

import java.lang.reflect.Proxy;

public class AnnotationFactoryT implements ValueVisitor<Object> {

    /**
     * Returns proxy of {@link java.lang.annotation.Annotation}. Resulting object will return nulls for
     * not initialized annotation params (unless default value provided) and is allowed
     * to return null for any non accessor methods.
     */
    @SuppressWarnings("unchecked")
    public <T extends java.lang.annotation.Annotation> T reflect(AnnotationT annotation) {
        Class<?> clazz = annotation.getType().getType();
        return (T) Proxy.newProxyInstance(
            clazz.getClassLoader(),
            new Class[]{clazz},
            (proxy, method, args) -> {
                if ("toString".equals(method.getName())) {
                    return clazz.getSimpleName();
                }
                if ("annotationType".equals(method.getName())) {
                    return clazz;
                }
                return annotation.getParam(method.getName()).map(param -> param.accept(this)).orElse(method.getDefaultValue());
            }
        );
    }

    @Override
    public Object visit(StringValue stringValue) {
        return stringValue.getValue();
    }

    @Override
    public Object visit(EnumValue enumValue) {
        return enumValue.getValue();
    }

    @Override
    public Object visit(PrimitiveValue primitiveValue) {
        return primitiveValue.getValue();
    }

    @Override
    public Object visit(ClassValue classValue) {
        return classValue.getValue();
    }

    @Override
    public Object visit(AnnotationT annotationValue) {
        return reflect(annotationValue);
    }

    @Override
    public Object visit(ListValue listValue) {
        return listValue.getValues().stream().map(value -> value.accept(this)).toArray();
    }
}