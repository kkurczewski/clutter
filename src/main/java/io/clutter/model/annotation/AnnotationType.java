package io.clutter.model.annotation;

import io.clutter.common.PrimitiveUtils;
import io.clutter.common.Varargs;
import io.clutter.model.annotation.param.AnnotationValue;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.hash;

final public class AnnotationType {

    private final Class<? extends Annotation> type;
    private final Map<String, AnnotationValue> params;

    public AnnotationType(Class<? extends Annotation> type, LinkedHashMap<String, AnnotationValue> params) {
        this.type = type;
        this.params = params;
    }

    public AnnotationType(Class<? extends Annotation> type) {
        this(type, new LinkedHashMap<>());
    }

    @Deprecated
    public static AnnotationType of(Class<? extends Annotation> type) {
        return new AnnotationType(type);
    }

    public Class<? extends Annotation> getType() {
        return type;
    }

    public Map<String, AnnotationValue> getParams() {
        return params;
    }

    public <T> Optional<T> getParam(String key) {
        return Optional.ofNullable(params.get(key)).map(AnnotationValue::getValue);
    }

    @SafeVarargs
    final public boolean isInstanceOf(Class<? extends Annotation> annotation, Class<? extends Annotation>... more) {
        return Varargs.concat(annotation, more).stream().anyMatch(type::equals);
    }

    /**
     * Returns proxy of {@link Annotation}. Resulting object will return nulls for
     * not initialized annotation params (unless default value provided) and is allowed
     * to return null for any non accessor methods.
     */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T reflect() {
        return (T) Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class[]{type},
                (proxy, method, args) -> {
                    if ("toString".equals(method.getName())) {
                        return type.getSimpleName();
                    }
                    if ("annotationType".equals(method.getName())) {
                        return type;
                    }
                    Object returnValue = getParam(method.getName()).orElseGet(method::getDefaultValue);
                    if (returnValue.getClass() != PrimitiveUtils.toBoxed(method.getReturnType()) && !method.getReturnType().isAssignableFrom(returnValue.getClass())) {
                        throw new AnnotationTypeMismatchException(method, returnValue.getClass().getCanonicalName());
                    }
                    return returnValue;
                }
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationType that = (AnnotationType) o;
        return type.equals(that.type) && params.equals(that.params);
    }

    @Override
    public int hashCode() {
        return hash(type, params);
    }

    @Override
    public String toString() {
        return String.format("AnnotationType{type=%s, values=%s}", type, params);
    }
}
