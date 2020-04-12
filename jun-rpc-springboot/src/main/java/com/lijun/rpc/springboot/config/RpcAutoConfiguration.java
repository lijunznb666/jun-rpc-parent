package com.lijun.rpc.springboot.config;

import com.lijun.rpc.client.RpcClient;
import com.lijun.rpc.registry.IRegistry;
import com.lijun.rpc.registry.ZookeeperRegistry;
import com.lijun.rpc.server.RpcServer;
import com.lijun.rpc.spring.ReferenceAnnotationBeanPostProcessor;
import com.lijun.rpc.spring.RpcApplicationListener;
import com.lijun.rpc.spring.ServiceAnnotationBeanPostProcessor;
import com.lijun.rpc.springboot.ServiceAnnotationBeanFactoryPostProcessor;
import com.lijun.rpc.springboot.properties.RegistryProperties;
import com.lijun.rpc.springboot.properties.ServerProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class Name RpcAutoConfiguration ...
 * 配置类 负责注入bean
 *
 * @author LiJun
 * Created on 2020/4/12 17:58
 */
@Configuration
@EnableConfigurationProperties({RegistryProperties.class, ServerProperties.class})
public class RpcAutoConfiguration {

    @Bean
    public IRegistry registry(RegistryProperties properties) throws Exception {
        ZookeeperRegistry registry = new ZookeeperRegistry(properties.getAddress());
        return registry;
    }

    @ConditionalOnProperty(prefix = "jun.rpc.server", value = "enable", havingValue = "true")
    @Bean
    public RpcServer rpcServer(IRegistry registry, ServerProperties properties) {
        RpcServer server = new RpcServer(registry);
        server.port(properties.getPort());
        return server;
    }

    @ConditionalOnProperty(prefix = "jun.rpc.server", value = "enable", havingValue = "true")
    @Bean
    public static ServiceAnnotationBeanFactoryPostProcessor beanFactoryPostProcessor(@Value("${jun.rpc.annotation.package}") String packageName) {
        ServiceAnnotationBeanFactoryPostProcessor processor = new ServiceAnnotationBeanFactoryPostProcessor(packageName);
        return processor;
    }

    @ConditionalOnProperty(prefix = "jun.rpc.server", value = "enable", havingValue = "true")
    @Bean
    public ServiceAnnotationBeanPostProcessor serviceAnnotationBeanPostProcessor() {
        ServiceAnnotationBeanPostProcessor processor = new ServiceAnnotationBeanPostProcessor();
        return processor;
    }

    @ConditionalOnProperty(prefix = "jun.rpc.client", value = "enable", havingValue = "true")
    @Bean
    public ReferenceAnnotationBeanPostProcessor referenceAnnotationBeanPostProcessor() {
        ReferenceAnnotationBeanPostProcessor processor = new ReferenceAnnotationBeanPostProcessor();
        return processor;
    }

    @ConditionalOnProperty(prefix = "jun.rpc.server", value = "enable", havingValue = "true")
    @Bean
    public RpcApplicationListener applicationListener() {
        RpcApplicationListener applicationListener = new RpcApplicationListener();
        return applicationListener;
    }

    @ConditionalOnProperty(prefix = "jun.rpc.client", value = "enable", havingValue = "true")
    @Bean
    public RpcClient client(IRegistry registry) {
        RpcClient client = new RpcClient(registry);
        return client;
    }
}
