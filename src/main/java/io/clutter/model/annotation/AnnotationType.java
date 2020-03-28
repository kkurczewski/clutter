package io.clutter.model.annotation;

import io.clutter.common.Varargs;
import io.clutter.model.annotation.param.AnnotationParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.hash;

final public class AnnotationType {

    private final Class<? extends Annotation> type;
    private final Map<String, ?> values;

    public AnnotationType(Class<? extends Annotation> type, LinkedHashMap<String, ?> values) {
        this.type = type;
        this.values = values;
    }

    public AnnotationType(Class<? extends Annotation> type) {
        this(type, new LinkedHashMap<>());
    }

    public static AnnotationType of(Class<? extends Annotation> type) {
        return new AnnotationType(type);
    }

    @Deprecated
    public static AnnotationType of(Class<? extends Annotation> type, AnnotationParam... values) {
        return new AnnotationType(type);
    }

    @Deprecated
    public static AnnotationType raw(String type, AnnotationParam... values) {
        return new AnnotationType(Deprecated.class, new LinkedHashMap<>());
    }

    public Class<? extends Annotation> getType() {
        return type;
    }

    public Map<String, ?> getParams() {
        return values;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getParam(String key) {
        return (Optional<T>) Optional.ofNullable(values.get(key));
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
        return (T) Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class[]{type},
                (proxy, method, args) -> {
                    if ("toString".equals(method.getName())) {
                        return format("Proxy{%s}", type);
                    }
                    return getParam(method.getName()).orElseGet(method::getDefaultValue);
                }
        );
    }

    /**
     * Returns class instance of underlying {@link Annotation}
     *
     * @throws RuntimeException when reflection related failure
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public <T extends Annotation> Class<T> reflectType() {
        try {
            return (Class<T>) Class.forName("Deprecated.class");
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
        return hash(type, values);
    }

    @Override
    public String toString() {
        return String.format("AnnotationType{type=%s, values=%s}", type, values);
    }
}
