package io.clutter.writer.model.type;

final public class WildcardType extends Type {

    public static final WildcardType ANY = WildcardType.alias("?");
    public static final WildcardType T = WildcardType.alias("T");
    public static final WildcardType U = WildcardType.alias("U");
    public static final WildcardType V = WildcardType.alias("V");
    public static final WildcardType R = WildcardType.alias("R");

    private boolean expanded;

    private WildcardType(String alias, boolean expanded) {
        super(alias, alias);
        this.expanded = expanded;
    }

    public static WildcardType alias(String alias) {
        return new WildcardType(alias, false);
    }

    public WildcardType extend(Class<?> exactType) {
        if (this.expanded) {
            throw new IllegalArgumentException("Illegal expand of generic type: " + super.value);
        }
        return new WildcardType(super.value + " extends " + exactType.getSimpleName(), true);
    }

    public WildcardType extend(WrappedType wrappedType) {
        if (this.expanded) {
            throw new IllegalArgumentException("Illegal expand of generic type: " + super.value);
        }
        return new WildcardType(super.value + " extends " + wrappedType.boxed, true);
    }

    public WildcardType subclass(Class<?> exactType) {
        if (this.expanded) {
            throw new IllegalArgumentException("Illegal expand of generic type: " + super.value);
        }
        return new WildcardType(super.value + " super " + exactType.getSimpleName(), true);
    }

    public WildcardType subclass(WrappedType wrappedType) {
        if (this.expanded) {
            throw new IllegalArgumentException("Illegal expand of generic type: " + super.value);
        }
        return new WildcardType(super.value + " super " + wrappedType.boxed, true);
    }
}
