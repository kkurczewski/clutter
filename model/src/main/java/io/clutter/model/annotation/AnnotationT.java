package io.clutter.model.annotation;

import io.clutter.model.type.BoxedType;
import io.clutter.model.value.Value;
import io.clutter.model.value.ValueVisitor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

final public class AnnotationT implements Value {

    private final BoxedType type;
    private final Map<String, Value> params;

    public AnnotationT(BoxedType type) {
        this.type = type;
        this.params = new LinkedHashMap<>();
    }

    public AnnotationT setParams(Consumer<Map<String, Value>> mutation) {
        mutation.accept(params);
        return this;
    }

    public Optional<Value> getParam(String key) {
        return Optional.ofNullable(params.get(key));
    }

    public Map<String, Value> getParams() {
        return new LinkedHashMap<>(params);
    }

    public BoxedType getType() {
        return type;
    }

    @Override
    public <T> T accept(ValueVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationT that = (AnnotationT) o;
        return type.equals(that.type) &&
            params.equals(that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "AnnotationT{" +
            "type=" + type +
            ", params=" + params +
            '}';
    }
}
