package com.lijun.rpc.spi.internal.utils;

/**
 * Class Name Holder ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:55
 */
public final class Holder<T> {

    /**
     * The value contained in the holder.
     */
    private volatile T value;

    /**
     * Creates a new holder with a <code>null</code> value.
     */
    public Holder() {
    }

    /**
     * Create a new holder with the specified value.
     *
     * @param value The value to be stored in the holder.
     */
    public Holder(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
