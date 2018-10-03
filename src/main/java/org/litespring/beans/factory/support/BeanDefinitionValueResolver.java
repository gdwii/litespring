package org.litespring.beans.factory.support;

import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;

public class BeanDefinitionValueResolver {
    private final BeanFactory beanFactory;

    public BeanDefinitionValueResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolve(Object value){
        if(value instanceof RuntimeBeanReference){
            RuntimeBeanReference beanReference = (RuntimeBeanReference)value;
            return beanFactory.getBean(beanReference.getBeanName());
        }else if(value instanceof TypedStringValue){
            TypedStringValue stringValue = (TypedStringValue)value;
            return stringValue.getValue();
        }else{
            throw new RuntimeException("the value " + value +" has not implemented");
        }
    }
}