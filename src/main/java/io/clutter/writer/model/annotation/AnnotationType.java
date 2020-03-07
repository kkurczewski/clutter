package io.clutter.writer.model.annotation;

import io.clutter.common.Varargs;
import io.clutter.writer.model.annotation.param.AnnotationParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.*;

import static java.lang.String.format;

final public class AnnotationType {

    private final String type;
    private final LinkedHashSet<AnnotationParam> values = new LinkedHashSet<>();

    private AnnotationType(String type, AnnotationParam... values) {
        this.type = type;
        Collections.addAll(this.values, values);
    }

    private AnnotationType(Class<? extends Annotation> type, AnnotationParam... values) {
        this(type.getCanonicalName(), values);
    }

    public static AnnotationType of(Class<? extends Annotation> type, AnnotationParam... values) {
        return new AnnotationType(type, values);
    }

    public static AnnotationType raw(String type, AnnotationParam... values) {
        return new AnnotationType(type, values);
    }

    public String getType() {
        return type;
    }

    public Set<AnnotationParam> getParams() {
        return values;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getParam(String key) {
        return (Optional<T>) values.stream()
                .filter(annotationParam -> annotationParam.getKey().equals(key))
                .findFirst()
                .map(AnnotationParam::getRawValue);
    }

    @SafeVarargs
    final public boolean isInstanceOf(Class<? extends Annotation> annotation, Class<? extends Annotation>... more) {
        return Varargs.concat(annotation, more)
                .stream()
                .map(Class::getCanonicalName)
                .anyMatch(type::equals);
    }

    /**
     * Returns shallow copy of {@link Annotation}. Resulting object will return nulls for not initialized annotation params and is allowed to return null for any non accessor methods
     *
     * @throws RuntimeException when reflection related failure
     */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T reflect() throws RuntimeException {
        Class<Annotation> type = reflectType();
        return (T) Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class[]{type},
                (proxy, method, args) -> {
                    if ("toString".equals(method.getName())) {
                        return format("Proxy{%s}", type);
                    }
                    return getParam(method.getName()).orElse(method.getDefaultValue());
                }
        );
    }

    /**
     * Returns class instance of underlying {@link Annotation}
     *
     * @throws RuntimeException when reflection related failure
     */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> Class<T> reflectType() {
        try {
            return (Class<T>) Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationType that = (AnnotationType) o;
        return type.equals(that.type) && values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, values);
    }

    @Override
    public String toString() {
        return type + values;
    }
}
