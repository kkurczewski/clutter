package io.clutter.javax.factory.visitors;

import io.clutter.javax.factory.AnnotationFactory;
import io.clutter.javax.factory.BoxedTypeFactory;
import io.clutter.javax.factory.TypeFactory;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor7;
import java.lang.reflect.Array;
import java.util.List;

public class AnnotationValueVisitor extends SimpleAnnotationValueVisitor7<Object, Class<?>> {

    @Override
    public Object visitType(TypeMirror t, Class<?> nothing) {
        return TypeFactory.from(t).getType();
    }

    @Override
    public Object visitEnumConstant(VariableElement c, Class<?> nothing) {
        Class<?> type = BoxedTypeFactory.from(c.asType()).getType();
        return createEnumInstance(type, c.getSimpleName().toString());
    }

    @Override
    public Object visitAnnotation(AnnotationMirror a, Class<?> nothing) {
        return AnnotationFactory.from(a).reflect();
    }

    @Override
    public Object visitArray(List<? extends AnnotationValue> vals, Class<?> type) {
        Object arr = Array.newInstance(type.getComponentType(), vals.size());
        for (int i = 0; i < vals.size(); i++) {
            Array.set(arr, i, vals.get(i).accept(this, null));
        }
        return arr;
    }

    @Override
    protected Object defaultAction(Object o, Class<?> nothing) {
        return o;
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T>> T createEnumInstance(Class<?> type, String name) {
        return Enum.valueOf((Class<T>) type, name);
    }
}
