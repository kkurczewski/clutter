package io.clutter.javax.factory.common;

import java.util.function.Function;

import static io.clutter.javax.factory.common.StringUtils.toCamelCase;
import static java.lang.String.format;

final public class NamingConventions {

    private static final String GET = "get";
    private static final String SET = "set";

    /**
     * Drop `get` prefix and convert first letter to lower case
     */
    public static Function<String, String> DROP_GET_PREFIX = dropPrefix(GET).andThen(StringUtils::toCamelCase);

    /**
     * Convert first letter to upper case and add `get` prefix
     */
    public static Function<String, String> ADD_GET_PREFIX = name -> addPrefix(GET).apply(toCamelCase(name));

    /**
     * Drop `set` prefix and convert first letter to lower case
     */
    public static Function<String, String> DROP_SET_PREFIX = dropPrefix(SET).andThen(StringUtils::toCamelCase);

    /**
     * Convert first letter to upper case and add `set` prefix
     */
    public static Function<String, String> ADD_SET_PREFIX = name -> addPrefix(SET).apply(toCamelCase(name));

    public static Function<String, String> dropPrefix(String prefix) {
        return name -> {
            if (!name.startsWith(prefix)) {
                throw new IllegalArgumentException(format("Expected name to start with `%s` prefix", prefix));
            }
            return name.substring(prefix.length());
        };
    }

    public static Function<String, String> addPrefix(String prefix) {
        return name -> prefix + name;
    }
}