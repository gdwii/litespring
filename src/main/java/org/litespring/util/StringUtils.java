package org.litespring.util;

public class StringUtils {
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }
}
