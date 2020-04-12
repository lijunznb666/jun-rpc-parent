package com.lijun.rpc.spi.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 和{@link MapSourceExtractor}配合使用，添加此注解，
 *
 * @author LiJun
 * @see MapSourceExtractor
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface FromAttribute {
    /**
     * 指定属性名，表示从所注解方法参数的这个属性上提取扩展实现名。
     *
     * @see com.lijun.rpc.spi.NameExtractor
     * @see MapSourceExtractor
     */
    String value();
}
