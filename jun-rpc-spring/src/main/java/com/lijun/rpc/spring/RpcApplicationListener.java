package com.lijun.rpc.spring;

import com.lijun.rpc.core.tookit.ExceptionUtils;
import com.lijun.rpc.server.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Class Name RpcApplicationListener ...
 *
 * @author LiJun
 * Created on 2020/4/8 20:22
 */
public class RpcApplicationListener implements ApplicationListener, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(RpcApplicationListener.class);

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        // 所有bean都已经实例化好了
        // 可以启动RpcServer了
        if (applicationEvent instanceof ContextRefreshedEvent) {
            RpcServer server = applicationContext.getBean(RpcServer.class);
            try {
                server.run();
            } catch (Exception e) {
                log.error("RpcApplicationListener onApplicationEvent error :", e);
                throw ExceptionUtils.mpe(e);
            }
        }
    }
}
