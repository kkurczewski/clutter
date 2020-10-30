package io.clutter.javax.factory;

import io.clutter.model.value.ClassValue;
import io.clutter.model.value.*;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor7;
import java.util.List;

import static java.util.stream.Collectors.toList;

final class JxAnnotationValueVisitor extends SimpleAnnotationValueVisitor7<Value, Class<?>> {

    @Override
    public Value visitType(TypeMirror t, Class<?> clazz) {
        return ClassValue.of(JxTypeAdapter.from(t));
    }

    @Override
    public Value visitEnumConstant(VariableElement c, Class<?> clazz) {
        return EnumValue.of(createEnumInstance(clazz, c.getSimpleName().toString()));
    }

    @Override
    public Value visitAnnotation(AnnotationMirror a, Class<?> clazz) {
        return JxAnnotationAdapter.from(a);
    }

    @Override
    public Value visitString(String s, Class<?> aClass) {
        return StringValue.of(s);
    }

    @Override
    public Value visitArray(List<? extends AnnotationValue> vals, Class<?> clazz) {
        return ListValue.of(vals.stream().map(val -> val.accept(this, clazz)).collect(toList()));
    }

    @Override
    protected Value defaultAction(Object o, Class<?> clazz) {
        return PrimitiveValue.of(o);
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T>> T createEnumInstance(Class<?> type, String name) {
        return Enum.valueOf((Class<T>) type, name);
    }
}