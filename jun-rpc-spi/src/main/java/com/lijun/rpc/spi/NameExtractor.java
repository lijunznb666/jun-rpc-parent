package com.lijun.rpc.spi;

import java.lang.reflect.Method;

/**
 * 从方法扩展点的方法参数中提取到扩展名称信息，在{@link Adaptive}中指定。
 * 会为每个扩展点的每个方法会对应一个{@link NameExtractor}实例。
 *
 * @see Adaptive
 * @since 0.3.0
 */
public interface NameExtractor {
    /**
     * {@link NameExtractor}实例对应扩展点方法，
     * 在创建{@link NameExtractor}实例时{@link ExtensionLoader}会调用此方法。
     */
    void setMethod(Method method);

    /**
     * After Properties set.
     */
    void init();

    /**
     * 从方法扩展点的方法参数中提取到扩展名称信息，由AdaptiveInstance调用此方法。
     *
     * @param argument 方法参数。
     * @return 返回提取到的扩展名称。<code>null</code>表示提取到的信息为空。
     */
    String extract(Object argument);
}
