package net.hollowcube.mql.util;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class StringUtil {
    private StringUtil() {}

    private static final @Language("regexp") String CAMEL_TO_SNAKE_CASE_REGEX = "([a-z])([A-Z]+)";
    private static final String CAMEL_TO_SNAKE_CASE_REPLACEMENT = "$1_$2";

    public static String camelCaseToSnakeCase(String str) {
        return str.replaceAll(CAMEL_TO_SNAKE_CASE_REGEX, CAMEL_TO_SNAKE_CASE_REPLACEMENT).toLowerCase();
    }
}
