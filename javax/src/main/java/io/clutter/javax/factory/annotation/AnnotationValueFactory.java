package io.clutter.javax.factory.annotation;

import io.clutter.model.annotation.param.AnnotationValue;

import java.lang.reflect.Constructor;

final class AnnotationValueFactory {

    static AnnotationValue ofRawObject(Object object) {
        try {
            // set value directly to avoid hassle with using regular methods
            // but keep constructor hidden for regular usage
            Constructor<AnnotationValue> constructor = AnnotationValue.class.getDeclaredConstructor(Object.class);
            constructor.setAccessible(true);
            return constructor.newInstance(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
