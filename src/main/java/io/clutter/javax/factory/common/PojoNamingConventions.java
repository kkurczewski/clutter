package io.clutter.javax.factory.common;

import static java.lang.String.format;

final public class PojoNamingConventions {

    public static PojoNamingConvention GET = prefix("get");
    public static PojoNamingConvention SET = prefix("set");

    public static PojoNamingConvention prefix(String prefix) {
        return new PojoNamingConvention() {
            @Override
            public String variable(String methodName) {
                if (!methodName.startsWith(prefix)) {
                    throw new IllegalArgumentException(format("Expected method to start with `%s` prefix", prefix));
                }
                return StringUtils.toLowerWordCase(methodName.substring(prefix.length()));
            }

            @Override
            public String method(String variableName) {
                return prefix + StringUtils.toUpperWordCase(variableName);
            }
        };
    }

    public static PojoNamingConvention plain() {
        return new PojoNamingConvention() {
            @Override
            public String variable(String methodName) {
                return methodName;
            }

            @Override
            public String method(String variableName) {
                return variableName;
            }
        };
    }
}
