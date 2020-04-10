package com.lijun.rpc.springboot.config;


import com.lijun.rpc.client.ConnectManager;
import com.lijun.rpc.client.RpcClient;
import com.lijun.rpc.registry.ServiceDiscovery;
import com.lijun.rpc.registry.ServiceRegistry;
import com.lijun.rpc.server.RpcServer;
import com.lijun.rpc.spring.ReferenceAnnotationBeanPostProcessor;
import com.lijun.rpc.spring.RpcApplicationListener;
import com.lijun.rpc.spring.ServiceAnnotationBeanPostProcessor;
import com.lijun.rpc.springboot.ServiceAnnotationBeanFactoryPostProcessor;
import com.lijun.rpc.springboot.properties.ClientProperties;
import com.lijun.rpc.springboot.properties.RegistryProperties;
import com.lijun.rpc.springboot.properties.ServerProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class Name RpcAutoConfiguration ...
 *
 * @author LiJun
 * Created on 2020/4/7 19:48
 */
@Configuration
@EnableConfigurationProperties({RegistryProperties.class, ServerProperties.class, ClientProperties.class})
public class RpcAutoConfiguration {

    @ConditionalOnProperty(prefix = "jun.rpc.server", value = "enable", havingValue = "true")
    @Bean
    public ServiceRegistry registry(RegistryProperties properties) throws Exception {
        return new ServiceRegistry(properties.getAddress());
    }

    @ConditionalOnProperty(prefix = "jun.rpc.client", value = "enable", havingValue = "true")
    @Bean
    public ServiceDiscovery discover(RegistryProperties properties) throws Exception {
        return new ServiceDiscovery(properties.getAddress());
    }

    @ConditionalOnProperty(prefix = "jun.rpc.server", value = "enable", havingValue = "true")
    @Bean
    public RpcServer rpcServer(ServiceRegistry registry, ServerProperties properties) {
        RpcServer server = new RpcServer(properties.getServerFactoryName(), registry);
        server.port(properties.getPort());
        return server;
    }

    @ConditionalOnProperty(prefix = "jun.rpc.server", value = "enable", havingValue = "true")
    @Bean
    public static ServiceAnnotationBeanFactoryPostProcessor beanFactoryPostProcessor(@Value("${jun.rpc.annotation.package}") String packageName) {
        return new ServiceAnnotationBeanFactoryPostProcessor(packageName);
    }

    @ConditionalOnProperty(prefix = "jun.rpc.server", value = "enable", havingValue = "true")
    @Bean
    public ServiceAnnotationBeanPostProcessor serviceAnnotationBeanPostProcessor() {
        return new ServiceAnnotationBeanPostProcessor();
    }

    @ConditionalOnProperty(prefix = "jun.rpc.client", value = "enable", havingValue = "true")
    @Bean
    public ReferenceAnnotationBeanPostProcessor referenceAnnotationBeanPostProcessor() {
        return new ReferenceAnnotationBeanPostProcessor();
    }

    @ConditionalOnProperty(prefix = "jun.rpc.server", value = "enable", havingValue = "true")
    @Bean
    public RpcApplicationListener applicationListener() {
        return new RpcApplicationListener();
    }

    @ConditionalOnProperty(prefix = "jun.rpc.client", value = "enable", havingValue = "true")
    @Bean
    public RpcClient client(ServiceDiscovery registry, ClientProperties properties) {
        ConnectManager.setFactoryName(properties.getConnectManagerFactoryName());
        return new RpcClient(properties.getClientFactoryName(), properties.getAddress(), registry);
    }
}
