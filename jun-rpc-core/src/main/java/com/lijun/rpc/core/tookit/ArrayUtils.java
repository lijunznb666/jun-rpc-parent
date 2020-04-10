package com.lijun.rpc.core.tookit;

/**
 * Class Name ArraysUtils ...
 * <p>
 * Contains some methods to check array
 *
 * @author LiJun
 * Created on 2020/3/9 16:39
 */
public class ArrayUtils {

    /**
     * Check if the array is null or empty
     * soft while array is null
     *
     * @param array the array which need to check
     * @return {@code true} if the array is null or empty
     */
    public static boolean isEmpty(final Object[] array) {
        return null == array || array.length <= 0;
    }

    /**
     * Check if the array is not null and not empty
     * soft while array is null
     *
     * @param array the array which need to check
     * @return {@code true} if the array is not null and not empty
     */
    public static boolean isNotEmpty(final Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 私有化构造函数 (make constructor private )
     */
    private ArrayUtils() {
    }
}
