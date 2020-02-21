package io.clutter.writer.common;

final public class StringUtils {

    public static String toUpperWordCase(String word) {
        String stringValue = String.valueOf(word);
        return String.valueOf(stringValue.charAt(0)).toUpperCase() + stringValue.substring(1);
    }

    public static String toLowerWordCase(String word) {
        String stringValue = String.valueOf(word);
        return String.valueOf(stringValue.charAt(0)).toLowerCase() + stringValue.substring(1);
    }
}
