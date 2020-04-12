

package com.lijun.rpc.spi.support;

import com.lijun.rpc.spi.internal.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 从{@link Map}参数中提取扩展名称。如果方法参数上还有{@link FromAttribute}注解，
 * 表示不是从参数上直接提取，而是从参数的指定属性上提取。
 * <p/>
 * 参数是{@link Map}类型，所以参数可以是<b>自定义</b>的类型，只要实现{@link Map}接口即可。
 * <p/>
 * 示例：
 * <pre><code>
 * public void method(@FromAttribute("parameters") @Adaptive Person person);
 * </code></pre>
 * 表示从方法参数<code>person</code>的<code>parameters</code>属性（是个Map）上提取。
 *
 * @author Lijun
 * @see FromAttribute
 * @since 0.3.0
 */
public class MapSourceExtractor extends AbstractNameExtractor {
    private FromAttribute fromAttribute = null;
    private Method fromGetter = null;

    @Override
    protected void doInit() {
        Annotation[] annotations = method.getParameterAnnotations()[adaptiveArgumentIndex];
        for (Annotation a : annotations) {
            if (a instanceof FromAttribute) {
                if (fromAttribute != null) {
                    throw new IllegalStateException("at most 1 " + FromAttribute.class.getSimpleName() +
                            " annotation is allowed!");
                }
                fromAttribute = (FromAttribute) a;
                if (Map.class.isAssignableFrom(parameterType)) {
                    throw new IllegalStateException("annotation " +
                            FromAttribute.class.getSimpleName() + " can only be used on POJO!");
                }
                String getter = StringUtils.attribute2Getter(fromAttribute.value());
                try {
                    fromGetter = parameterType.getMethod(getter);
                } catch (NoSuchMethodException e) {
                    throw new IllegalStateException("No " + getter + " method for parameter type " +
                            parameterType.getName() + " on method " + method.getName() +
                            " of extension " + extension.getName());
                }
            }
        }
    }

    @Override
    public String extract(Object argument) {
        if (argument == null) {
            throw new IllegalArgumentException("adaptive " + parameterType.getName() +
                    " argument == null");
        }

        Object sourceObject = argument;
        if (fromAttribute != null) {
            try {
                sourceObject = fromGetter.invoke(sourceObject);
                if (null == sourceObject) {
                    throw new IllegalArgumentException("adaptive " + parameterType.getName() +
                            " argument " + fromGetter.getName() + "() == null");
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Fail to get attribute " +
                        fromGetter.getName() + ", cause: " + e.getMessage(), e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException("Fail to get attribute " +
                        fromGetter.getName() + ", cause: " + e.getMessage(), e);
            }
        }
        return getFromMap(sourceObject, adaptiveKeys);
    }

    private static String getFromMap(Object obj, String[] keys) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) obj;
        for (String key : keys) {
            Object value = map.get(key);
            if (value == null) {
                continue;
            }
            return value.toString();
        }
        return null;
    }
}
