package io.clutter.javax.factory.common;

import java.util.stream.Collectors;

import static java.lang.String.valueOf;

final public class StringUtils {

    public static String toSnakeCase(String word) {
        return word.chars()
                .mapToObj(ch -> (char) ch)
                .map(ch -> Character.isUpperCase(ch)
                        ? "_" + valueOf((char) ch).toLowerCase()
                        : valueOf((char) ch))
                .collect(Collectors.joining());
    }

    public static String toPascalCase(String word) {
        String firstChar = valueOf(word.charAt(0)).toUpperCase();
        return firstChar + word.substring(1);
    }

    public static String toCamelCase(String word) {
        String firstChar = valueOf(word.charAt(0)).toLowerCase();
        return firstChar + word.substring(1);
    }

}
