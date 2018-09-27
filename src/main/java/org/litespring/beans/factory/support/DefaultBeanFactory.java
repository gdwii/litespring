package org.litespring.beans.factory.support;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.util.ClassUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory implements BeanFactory, BeanDefinitionRegistry {
    private final Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    public BeanDefinition getBeanDefine(String beanId) {
        return beanDefinitions.get(beanId);
    }

    public Object getBean(String beanId) {
        BeanDefinition beanDefinition = getBeanDefine(beanId);
        if(beanDefinition == null){
            throw new BeanCreationException("Bean Definition does not exist");
        }
        String beanClassName = beanDefinition.getBeanClassName();
        try {
            return Class.forName(beanClassName).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("create bean for " + beanClassName + " fail", e);
        }
    }

    @Override
    public void registerBeanDefinition(String beanId, BeanDefinition beanDefinition) {
        beanDefinitions.put(beanId, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanId) {
        return beanDefinitions.get(beanId);
    }
}
