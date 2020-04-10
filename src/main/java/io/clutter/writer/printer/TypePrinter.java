package io.clutter.writer.printer;

import io.clutter.model.type.*;
import io.clutter.writer.Imports;

import java.util.Collection;

import static java.lang.String.format;

public class TypePrinter {

    public static final String DEFAULT_IMPORT = "java.lang";

    private final Imports imports;

    TypePrinter() {
        this.imports = new Imports();
    }

    public TypePrinter(Imports imports) {
        this.imports = imports;
    }

    public <T extends Type> String print(T type) {
        if (type instanceof ContainerType) {
            var genericValues = ((ContainerType) type).genericValues();
            return print(type.getType()) + printGenerics(genericValues);
        } else if (type instanceof BoundedWildcardType) {
            var wildcard = (BoundedWildcardType) type;
            return format("%s %s %s", wildcard.getAlias(), wildcard.getBoundaryKeyword(), print(wildcard.getBound()));
        } else if (type instanceof WildcardType) {
            return ((WildcardType) type).getAlias();
        } else if (type instanceof BoxedType) {
            return print(type.getType());
        } else if (type instanceof DynamicType) {
            return ((DynamicType) type).getName();
        }
        return type.getType().getSimpleName();
    }

    <T extends Enum<?>> String print(T enumerated) {
        return useSimpleName(enumerated.getClass())
                ? enumerated.name()
                : enumerated.getClass().getCanonicalName() + '.' + enumerated.name();
    }

    public String printGenerics(Collection<? extends BoxedType> boxedType) {
        return boxedType.stream()
                .map(this::print)
                .reduce((first, second) -> first + ", " + second)
                .map(type -> "<" + type + ">")
                .orElse("");
    }

    String print(Class<?> clazz) {
        var rawClass = clazz;
        while (rawClass.isArray()) {
            rawClass = rawClass.getComponentType();
        }
        return rawClass.isPrimitive() || rawClass.getPackageName().equals(DEFAULT_IMPORT) || useSimpleName(rawClass)
                ? clazz.getSimpleName()
                : clazz.getCanonicalName();
    }

    public Imports getImports() {
        return imports;
    }

    protected boolean useSimpleName(Class<?> clazz) {
        return imports.contains(clazz);
    }
}
