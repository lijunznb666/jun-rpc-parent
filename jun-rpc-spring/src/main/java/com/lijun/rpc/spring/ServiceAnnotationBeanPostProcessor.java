package com.lijun.rpc.spring;

import com.lijun.rpc.core.annotation.Service;
import com.lijun.rpc.core.tookit.ExceptionUtils;
import com.lijun.rpc.server.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Class Name ServiceAnnotationBeanPostProcessor ...
 *
 * @author LiJun
 * Created on 2020/4/8 20:23
 */
public class ServiceAnnotationBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(ServiceAnnotationBeanPostProcessor.class);

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Service.class)) {
            RpcServer rpcServer = applicationContext.getBean(RpcServer.class);
            Service serviceClass = beanClass.getDeclaredAnnotation(Service.class);
            try {
                rpcServer.exposeService(serviceClass.interfaceClass(), bean);
            } catch (Exception e) {
                log.error("ServiceAnnotationBeanPostProcessor postProcessAfterInitialization error :", e);
                throw ExceptionUtils.mpe(e);

            }
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
