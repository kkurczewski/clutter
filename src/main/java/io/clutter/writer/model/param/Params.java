package io.clutter.writer.model.param;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

final public class Params {

    private final LinkedHashMap<String, String> map;

    public Params() {
        this.map = new LinkedHashMap<>();
    }

    public static Params empty() {
        return new Params();
    }

    public static Params just(String name, String type) {
        return new Params().add(name, type);
    }

    public Params add(String name, String type) {
        map.put(name, type);

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
        Params params = (Params) o;
        return map.equals(params.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        return String.valueOf(map);
    }
}
