package com.lijun.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * Class Name Service ...
 *
 * @author LiJun
 * Created on 2020/4/6 10:28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Service {
    Class<?> interfaceClass() default void.class;
}
