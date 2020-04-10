package com.lijun.rpc.spi;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 把一个接口标识成扩展点。
 * <p/>
 * 没有此注释的接口{@link ExtensionLoader}会拒绝接管。
 *
 * @author Jerry Lee(oldratlee AT gmail DOT com)
 * @see ExtensionLoader
 * @since 0.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Extension {

    /**
     * the default extension name.
     *
     * @since 0.1.0
     */
    String defaultValue() default "";

}
