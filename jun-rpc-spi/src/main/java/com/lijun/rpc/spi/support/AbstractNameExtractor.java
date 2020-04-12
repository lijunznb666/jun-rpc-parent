package com.lijun.rpc.spi.support;


import com.lijun.rpc.spi.Adaptive;
import com.lijun.rpc.spi.NameExtractor;
import com.lijun.rpc.spi.internal.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author LiJun
 */
public abstract class AbstractNameExtractor implements NameExtractor {
    protected Method method;

    protected Class<?> extension;

    protected int adaptiveArgumentIndex;
    protected Class<?> parameterType;
    protected Adaptive adaptive;

    protected String[] adaptiveKeys;

    @Override
    public void setMethod(Method method) {
        this.method = method;
        this.extension = method.getDeclaringClass();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            for (Annotation a : annotations) {
                if (a instanceof Adaptive) {
                    adaptiveArgumentIndex = i;
                    parameterType = method.getParameterTypes()[i];
                    adaptive = (Adaptive) a;
                    break;
                }
            }
        }
    }

    public final void init() {
        String[] keys = adaptive.value();
        if (keys.length == 0) {
            // 没有设置Key，则使用“扩展点接口名的点分隔”作为Key
            keys = new String[]{StringUtils.toDotSpiteString(extension.getSimpleName())};
        }
        adaptiveKeys = keys;

        doInit();
    }

    protected abstract void doInit();

    @Override
    public abstract String extract(Object argument);
}
