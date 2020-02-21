package io.clutter.writer.common;

import static java.lang.String.format;

final public class PojoNamingConventions {

    public static PojoNamingConvention prefix(String prefix) {
        return new PojoNamingConvention() {
            @Override
            public String variable(String methodName) {
                if (!methodName.startsWith(prefix)) {
                    throw new IllegalArgumentException(format("Expected method to start with `%s` prefix", prefix));
                }
                return StringUtils.toLowerWordCase(methodName.substring(methodName.length()));
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
