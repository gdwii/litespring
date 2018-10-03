package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends DefaultSingletonBeanRegistry
        implements ConfigurableBeanFactory, BeanDefinitionRegistry {
    private final Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    private ClassLoader beanClassLoader;

    public Object getBean(String beanId) {
        BeanDefinition beanDefinition = getBeanDefinition(beanId);
        if(beanDefinition == null){
            throw new BeanCreationException("Bean Definition does not exist");
        }
        if(beanDefinition.isSingleton()){
            return getSingleton(beanId, beanDefinition);
        }
        return createBean(beanDefinition);
    }

    private Object getSingleton(String beanId, BeanDefinition beanDefinition) {
        Object singletonObject = getSingleton(beanId);
        if(singletonObject == null){
            singletonObject = createBean(beanDefinition);
            registerSingleton(beanId, singletonObject);
        }
        return singletonObject;
    }


    private Object createBean(BeanDefinition beanDefinition) {
        // 1.创建Bean实例
        Object bean = instantiateBean(beanDefinition);
        // 2.设置Bean的属性
        populateBean(beanDefinition, bean);
        return bean;
    }

    private void populateBean(BeanDefinition beanDefinition, Object bean) {
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        if(propertyValues == null || propertyValues.isEmpty()){
            return ;
        }

        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());

            for(PropertyValue propertyValue : propertyValues){
                PropertyDescriptor propertyDescriptor = getPropertyDescriptor(beanInfo, propertyValue.getName());
                Object resolvedValue = valueResolver.resolveValueIfNecessary(propertyValue.getValue());
                propertyDescriptor.getWriteMethod().invoke(bean, resolvedValue);
            }
        } catch (Exception e) {
            throw new BeanCreationException("Failed to obtain BeanInfo for class [" + beanDefinition.getBeanClassName() + "]", e);
        }
    }

    private PropertyDescriptor getPropertyDescriptor(BeanInfo beanInfo, String propertyName) {
        for(PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()){
            if(propertyDescriptor.getName().equals(propertyName)){
                return propertyDescriptor;
            }
        }
        return null;
    }


    private Object instantiateBean(BeanDefinition beanDefinition) {
        String beanClassName = beanDefinition.getBeanClassName();
        try {
            Class<?> beanClass = getBeanClassLoader().loadClass(beanClassName);
            return beanClass.getDeclaredConstructor().newInstance();
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

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return beanClassLoader != null ? beanClassLoader : ClassUtils.getDefaultClassLoader();
    }
}
