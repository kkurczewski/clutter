package io.clutter.model.common;

final public class Expression {

    private final String expression;

    private Expression(String expression) {
        this.expression = expression;
    }

    public static Expression fromString(String expression) {
        return new Expression(expression);
    }

    public static Expression empty() {
        return Expression.fromString("");
    }

    public String asString() {
        return expression;
    }
}
