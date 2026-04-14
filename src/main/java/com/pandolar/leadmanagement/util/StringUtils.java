package com.pandolar.leadmanagement.util;

public final class StringUtils {

    private StringUtils() {
    }

    public static String trimOrNull(String value) {
        return value != null ? value.trim() : null;
    }

    public static String normalizeEmail(String email) {
        return email != null ? email.trim().toLowerCase() : null;
    }
}
