package io.clutter.writer;

import io.clutter.model.type.*;

import java.util.List;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class TypePrinter {

    public static final String DEFAULT_IMPORT = "java.lang";

    public TypePrinter() {
    }

    final public TypePrinter useCanonicalName() {
        final var parent = this;
        return new TypePrinter() {
            @Override
            protected String className(Class<?> javaType) {
                return javaType.getPackageName().equals(DEFAULT_IMPORT)
                        ? javaType.getSimpleName()
                        : javaType.getCanonicalName();
            }

            @Override
            protected <T extends BoxedType> String genericParameters(List<T> genericValues, Function<T, String> print) {
                return parent.genericParameters(genericValues, this::print);
            }
        };
    }

    final public TypePrinter useDiamondOperator() {
        final var parent = this;
        return new TypePrinter() {
            @Override
            protected String className(Class<?> javaType) {
                return parent.className(javaType);
            }

            @Override
            protected <T extends BoxedType> String genericParameters(List<T> genericValues, Function<T, String> print) {
                return "<>";
            }
        };
    }

    final public <T extends Type> String print(T type) {
        if (type instanceof DynamicType) {
            return ((DynamicType) type).getName();
        } else if (type instanceof ContainerType) {
            return className(type.getType()) + genericParameters(((ContainerType) type).genericValues(), this::print);
        } else if (type instanceof BoundedWildcardType) {
            BoundedWildcardType wildcard = (BoundedWildcardType) type;
            return format("%s %s %s", wildcard.getAlias(), wildcard.getBoundaryKeyword(), print(wildcard.getBound()));
        } else if (type instanceof WildcardType) {
            return ((WildcardType) type).getAlias();
        }
        if (type instanceof BoxedType) {
            return className(type.getType());
        }
        return type.getType().getSimpleName();
    }

    protected <T extends BoxedType> String genericParameters(List<T> genericValues, Function<T, String> print) {
        return genericValues
                .stream()
                .map(print)
                .collect(joining(", ", "<", ">"));
    }

    protected String className(Class<?> javaType) {
        return javaType.getSimpleName();
    }
}
