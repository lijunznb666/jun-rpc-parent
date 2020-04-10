package com.lijun.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * Class Name Reference ...
 *
 * @author LiJun
 * Created on 2020/4/6 10:28
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Reference {
    Class<?> interfaceClass();
}
