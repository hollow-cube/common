package net.hollowcube.mql.util;

import org.intellij.lang.annotations.Language;

public final class StringUtil {
    private StringUtil() {}

    private static final @Language("regexp") String CAMEL_TO_SNAKE_CASE_REGEX = "([a-z])([A-Z]+)";
    private static final String CAMEL_TO_SNAKE_CASE_REPLACEMENT = "$1_$2";

    public static String camelCaseToSnakeCase(String str) {
        return str.replaceAll(CAMEL_TO_SNAKE_CASE_REGEX, CAMEL_TO_SNAKE_CASE_REPLACEMENT).toLowerCase();
    }

    public static String snakeCaseToCamelCase(String str) {
        while (str.contains("_")) {
            str = str.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(str.charAt(str.indexOf("_") + 1))));
        }
        return str;
    }
}
