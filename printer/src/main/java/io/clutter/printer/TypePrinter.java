package io.clutter.printer;

import io.clutter.model.type.*;

import static java.util.stream.Collectors.joining;

final public class TypePrinter implements TypeVisitor<String> {

    private final PackagePrinter packagePrinter;

    public TypePrinter(PackagePrinter packagePrinter) {
        this.packagePrinter = packagePrinter;
    }

    @Override
    public String visit(ContainerType type) {
        return packagePrinter.printClass(type.getType()) + type.genericValues()
            .stream()
            .map(genericType -> genericType.accept(this))
            .collect(joining(", ", "<", ">"));
    }

    @Override
    public String visit(DynamicType type) {
        return type.getName();
    }

    @Override
    public String visit(BoundedGenericType type) {
        return String.join(
            " ",
            type.getAlias(),
            type.getBoundaryKeyword().name().toLowerCase(),
            visit(type.getBound())
        );
    }

    @Override
    public String visit(GenericType type) {
        return type.getAlias();
    }

    @Override
    public String fallback(Type type) {
        return packagePrinter.printClass(type.getType());
    }
}
