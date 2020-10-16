package io.clutter.model.z.model.annotation;

import io.clutter.model.z.model.value.Value;

import java.util.*;
import java.util.function.Consumer;

final public class Annotation {

    private final Class<? extends java.lang.annotation.Annotation> type;
    private final Map<String, List<Value>> params;

    public Annotation(Class<? extends java.lang.annotation.Annotation> type) {
        this.type = type;
        this.params = new LinkedHashMap<>();
    }

    public Annotation setParams(Consumer<Map<String, List<Value>>> mutation) {
        mutation.accept(params);
        return this;
    }

    public Map<String, List<Value>> getParams() {
        return new LinkedHashMap<>(params);
    }

    public Class<? extends java.lang.annotation.Annotation> getType() {
        return type;
    }
}
