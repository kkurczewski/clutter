package io.clutter.writer.model.annotation;

import io.clutter.common.Varargs;
import io.clutter.writer.model.annotation.param.AnnotationParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.*;

import static java.util.Arrays.stream;

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

    public Optional<Object> getParam(String key) {
        return values.stream().filter(annotationParam -> annotationParam.getKey().equals(key)).findFirst().map(AnnotationParam::getRawValue);
    }

    @SafeVarargs
    final public boolean isInstanceOf(Class<? extends Annotation> annotation, Class<? extends Annotation>... more) {
        return stream(Varargs.concat(annotation, more))
                .map(Class::getCanonicalName)
                .anyMatch(type::equals);
    }

    /**
     * Returns shallow copy of {@link Annotation}. Resulting object will return nulls for not initialized annotation params and is guaranteed to return null for non accessor methods (including common method like {@link Object#toString()})
     *
     * @throws RuntimeException when reflection related failure
     */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T reflect() throws RuntimeException {
        try {
            return (T) Proxy.newProxyInstance(
                    this.getClass().getClassLoader(),
                    new Class[]{Class.forName(type, false, this.getClass().getClassLoader())},
                    (proxy, method, args) -> getParam(method.getName()).orElse(method.getDefaultValue())
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns class instance of underlying {@link Annotation}
     *
     * @throws RuntimeException when reflection related failure
     */
    @SuppressWarnings("unchecked")
    public Class<? extends Annotation> reflectType() {
        try {
            return (Class<? extends Annotation>) Class.forName(type, false, this.getClass().getClassLoader());
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
