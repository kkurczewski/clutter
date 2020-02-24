package io.clutter.writer.common;

final public class StringUtils {

    public static String toUpperWordCase(String word) {
        String firstChar = String.valueOf(word.charAt(0)).toUpperCase();
        return firstChar + word.substring(1);
    }

    public static String toLowerWordCase(String word) {
        String firstChar = String.valueOf(word.charAt(0)).toLowerCase();
        return firstChar + word.substring(1);
    }
}
