package com.lijun.rpc.springboot;

import com.lijun.rpc.core.annotation.Service;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * Class Name ServiceAnnotationScanner ...
 *
 * @author LiJun
 * Created on 2020/4/12 17:59
 */
public class ServiceAnnotationScanner extends ClassPathBeanDefinitionScanner {
    public ServiceAnnotationScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public void registerDefaultFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(Service.class));
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
//        for (BeanDefinitionHolder holder : beanDefinitions) {
//            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
//            definition.getPropertyValues().add("innerClassName", definition.getBeanClassName());
//            definition.setBeanClass(FactoryBeanTest.class);
//        }
        return beanDefinitions;
    }
}
