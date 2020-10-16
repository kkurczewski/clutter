package io.clutter.model.z.model.common;

public class Expression {

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

    public String getExpression() {
        return expression;
    }
}
