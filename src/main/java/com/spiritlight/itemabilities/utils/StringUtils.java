package com.spiritlight.itemabilities.utils;

public class StringUtils {
    public static String stringOrNull(String s) {
        return s == null ? "null" : s;
    }

    public static String nullOrDefault(String s, String _default) {
        return s == null ? stringOrNull(_default) : s;
    }
}
