package io.clutter.javax.factory.visitors;

import io.clutter.javax.factory.AnnotationFactory;
import io.clutter.javax.factory.types.TypeFactory;
import io.clutter.model.annotation.param.AnnotationValue;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor7;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.List;

public class AnnotationValueVisitor extends SimpleAnnotationValueVisitor7<AnnotationValue, Class<?>> {

    @Override
    public AnnotationValue visitType(TypeMirror t, Class<?> clazz) {
        return defaultAction(TypeFactory.from(t).getType(), clazz);
    }

    @Override
    public AnnotationValue visitEnumConstant(VariableElement c, Class<?> clazz) {
        return defaultAction(createEnumInstance(clazz, c.getSimpleName().toString()), clazz);
    }

    @Override
    public AnnotationValue visitAnnotation(AnnotationMirror a, Class<?> clazz) {
        return defaultAction(AnnotationFactory.from(a).reflect(), clazz);
    }

    @Override
    public AnnotationValue visitArray(List<? extends javax.lang.model.element.AnnotationValue> vals, Class<?> clazzArr) {
        Class<?> clazz = clazzArr.getComponentType();
        Object arr = Array.newInstance(clazz, vals.size());
        for (int i = 0; i < vals.size(); i++) {
            Array.set(arr, i, vals.get(i).accept(this, clazz).getValue());
        }
        return defaultAction(arr, clazzArr);
    }

    @Override
    protected AnnotationValue defaultAction(Object o, Class<?> clazz) {
        try {
            // set value directly to avoid hassle with using regular methods
            // but keep constructor hidden for regular usage
            Constructor<AnnotationValue> constructor = AnnotationValue.class.getDeclaredConstructor(Object.class);
            constructor.setAccessible(true);
            return constructor.newInstance(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T>> T createEnumInstance(Class<?> type, String name) {
        return Enum.valueOf((Class<T>) type, name);
    }
}