package com.lijun.rpc.core.tookit;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

/**
 * Class Name CollectionUtils ...
 * Contains some methods to check collection
 *
 * @author LiJun
 * Created on 2020/3/9 16:49
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    private static final Comparator<String> SIMPLE_NAME_COMPARATOR = (s1, s2) -> {
        if (null == s1 && null == s2) {
            return 0;
        }
        if (null == s1) {
            return -1;
        }
        if (null == s2) {
            return 1;
        }
        int index1 = s1.lastIndexOf('.');
        if (index1 >= 0) {
            s1 = s1.substring(index1 + 1);
        }
        int index2 = s2.lastIndexOf('.');
        if (index2 >= 0) {
            s2 = s2.substring(index2 + 1);
        }
        return s1.compareToIgnoreCase(s2);
    };

    /**
     * Check if the collection is null or empty
     *
     * @param collection the collection to check
     * @return {@code true} if the collection is null or empty
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }


    /**
     * Check if the map is null or empty
     *
     * @param map the collection to check
     * @return {@code true} if the map is null or empty
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    /**
     * Check if the collection is not null and not empty
     *
     * @param collection the collection to check
     * @return {@code true} if the collection is not null and not empty
     */
    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Check if the map is not null and not empty
     *
     * @param map the map to check
     * @return {@code true} if the map is not null and not empty
     */
    public static boolean isNotEmpty(final Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * Check if the map is null or empty
     *
     * @param map the map to check
     * @return {@code true} if the map is null or empty
     */
    public static boolean isEmptyMap(final Map<?, ?> map) {
        return null == map || map.size() <= 0;
    }

    /**
     * Check if the map is not null and not empty
     *
     * @param map the map to check
     * @return {@code true} if the map is not null and not empty
     */
    public static boolean isNotEmptyMap(final Map<?, ?> map) {
        return !isEmptyMap(map);
    }

    /**
     * get the size of the collection
     *
     * @param collection the collection
     * @return this size of collection
     */
    public static int size(Collection<?> collection) {
        return null == collection ? 0 : collection.size();
    }

    /**
     * check the two collections are equals
     *
     * @param one the one
     * @param two the two
     * @return {@code true} if equals
     */
    public static boolean equals(Collection<?> one, Collection<?> two) {
        if (one == two) {
            return true;
        }
        if (isEmpty(one) && isEmpty(two)) {
            return true;
        }

        if (size(one) != size(two)) {
            return false;
        }

        return isEmpty(one) ? two.containsAll(one) : one.containsAll(two);
    }
}
