package io.clutter.writer.model.annotation.param;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.valueOf;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

final public class AnnotationParams {

    private final LinkedHashMap<String, String> map;

    public AnnotationParams() {
        this.map = new LinkedHashMap<>();
    }

    public static AnnotationParams empty() {
        return new AnnotationParams();
    }

    public static AnnotationParams just(String name, AnnotationAttribute param) {
        return new AnnotationParams().add(name, param);
    }

    /**
     * @throws IllegalArgumentException when params array is empty
     */
    public AnnotationParams add(String name, Object... params) {
        if (params.length == 0) {
            throw new IllegalArgumentException("AnnotationParam is empty");
        }
        final String value = params.length > 1
                ? stream(params).map(String::valueOf).collect(joining(", ", "{", "}"))
                : valueOf(params[0]);
        map.put(name, value);

        return this;
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return map.entrySet();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationParams that = (AnnotationParams) o;
        return map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
