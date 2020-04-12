package com.lijun.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * Class Name Reference ...
 * 引用类，通过@Reference 注入配置
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
