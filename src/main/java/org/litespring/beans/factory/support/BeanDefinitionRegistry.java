package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanId, BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(String beanId);
}
