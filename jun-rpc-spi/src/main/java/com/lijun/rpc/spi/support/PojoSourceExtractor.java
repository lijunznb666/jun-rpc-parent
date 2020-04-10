package com.lijun.rpc.spi.support;


import com.lijun.rpc.spi.internal.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 缺省AdaptiveInstance调用时，扩展名称提取方法。
 * <p/>
 * <ol>
 * <li> 有{@link com.leibangzhu.coco.Adaptive}注解的参数是String类型，则参数值直接作为扩展名称。
 * <li> 有{@link com.leibangzhu.coco.Adaptive}注解的参数是Map类型，则提取Map的Value作为扩展名称。
 * <li>
 * </ol>
 *
 * @author Jerry Lee(oldratlee AT gmail DOT com)
 * @since 0.3.0
 */
public class PojoSourceExtractor extends AbstractNameExtractor {
    private List<Method> pojoGetters;

    @Override
    public void doInit() {

        pojoGetters = new ArrayList<Method>();

        Method[] methods = parameterType.getMethods();
        for (String key : adaptiveKeys) {
            final String getterName = StringUtils.attribute2Getter(key);
            Method getter = null;
            for (Method method : methods) {
                if (getterName.equals(method.getName()) &&
                        !Modifier.isStatic(method.getModifiers()) &&
                        method.getParameterTypes().length == 0) {
                    getter = method;
                }
            }
            // 如果Key对应的方法不存在，则异常！
            if (getter == null) {
                throw new IllegalStateException("No getter method " + getterName +
                        " on parameter type " + parameterType + " to key " + key +
                        " from adaptive keys " + Arrays.toString(adaptiveKeys));
            }
            pojoGetters.add(getter);
        }
    }

    public String extract(Object argument) {
        if (argument == null) {
            throw new IllegalArgumentException("adaptive " + parameterType.getName() +
                    " argument == null");
        }
        return getFromPojo(argument, pojoGetters);
    }

    private static String getFromPojo(Object obj, List<Method> getter) {
        for (Method method : getter) {
            try {
                Object ret = method.invoke(obj);
                if (null != ret) {
                    return (String) ret;
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Fail to value via method " +
                        method.getName() + ", cause: " + e.getMessage(), e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException("Fail to value via method " +
                        method.getName() + ", cause: " + e.getMessage(), e);
            }
        }
        return null;
    }
}
