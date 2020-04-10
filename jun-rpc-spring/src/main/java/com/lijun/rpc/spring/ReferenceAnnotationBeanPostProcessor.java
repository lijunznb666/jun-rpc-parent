package com.lijun.rpc.spring;

import com.lijun.rpc.client.RpcClient;
import com.lijun.rpc.core.annotation.Reference;
import com.lijun.rpc.core.tookit.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

/**
 * Class Name ReferenceAnnotationBeanPostProcessor ...
 *
 * @author LiJun
 * Created on 2020/4/8 20:22
 */
public class ReferenceAnnotationBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(ReferenceAnnotationBeanPostProcessor.class);

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(Reference.class)) {
                Reference reference = field.getAnnotation(Reference.class);
                field.setAccessible(true);
                try {
                    field.set(bean, applicationContext.getBean(RpcClient.class).create(reference.interfaceClass()));
                } catch (Exception e) {
                    log.error("ReferenceAnnotationBeanPostProcessor postProcessAfterInitialization error :", e);
                    throw ExceptionUtils.mpe(e);

                }
            }
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
