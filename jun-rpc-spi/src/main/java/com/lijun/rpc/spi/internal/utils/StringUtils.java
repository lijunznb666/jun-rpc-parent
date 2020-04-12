package com.lijun.rpc.spi.internal.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Class Name StringUtils ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:55
 */
public final class StringUtils {
    /**
     * Get the String of Throwable, like the output of {@link Throwable#printStackTrace()}.
     *
     * @param throwable the input throwable.
     */
    public static String toString(Throwable throwable) {
        return toString(null, throwable);
    }

    /**
     * Get the String of Throwable, like the output of {@link Throwable#printStackTrace()}.
     *
     * @param head      the head line of message.
     * @param throwable the input throwable.
     */
    public static String toString(String head, Throwable throwable) {
        StringWriter w = new StringWriter(1024);
        if (head != null) {
            w.write(head + "\n");
        }
        PrintWriter p = new PrintWriter(w);
        try {
            throwable.printStackTrace(p);
            return w.toString();
        } finally {
            p.close();
        }
    }

    /**
     * convert CamelCase string to dot-split lowercase string.
     * <p/>
     * eg: convert <code>LessIsMore</code> to <code>less.is.more</code>
     */
    public static String toDotSpiteString(String input) {
        char[] charArray = input.toCharArray();
        StringBuilder sb = new StringBuilder(128);
        for (int i = 0; i < charArray.length; i++) {
            if (Character.isUpperCase(charArray[i])) {
                if (i != 0 && charArray[i - 1] != '.') {
                    sb.append('.');
                }
                sb.append(Character.toLowerCase(charArray[i]));
            } else {
                sb.append(charArray[i]);
            }
        }
        return sb.toString();
    }

    /**
     * name => getName
     */
    public static String attribute2Getter(String attribute) {
        return "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
    }

    private StringUtils() {
    }
}
