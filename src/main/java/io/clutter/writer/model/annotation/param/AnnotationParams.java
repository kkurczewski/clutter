package io.clutter.writer.model.annotation.param;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

final public class AnnotationParams {

    private final LinkedHashMap<String, AnnotationParam[]> map;

    public AnnotationParams() {
        this.map = new LinkedHashMap<>();
    }

    public static AnnotationParams empty() {
        return new AnnotationParams();
    }

    public static AnnotationParams just(String name, AnnotationParam param) {
        return new AnnotationParams().add(name, param);
    }

    /**
     * @throws IllegalArgumentException when params array is empty
     */
    public AnnotationParams add(String name, AnnotationParam... params) {
        if (params.length == 0) {
            throw new IllegalArgumentException("AnnotationParam is empty");
        }
        map.put(name, params);

        return this;
    }

    public Set<Map.Entry<String, AnnotationParam[]>> entrySet() {
        return map.entrySet();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationParams params = (AnnotationParams) o;
        return map.equals(params.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
