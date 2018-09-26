package org.litespring.beans.factory;

import org.litespring.beans.BeanDefinition;

public interface BeanFactory {
    BeanDefinition getBeanDefine(String beanId);

    Object getBean(String petStore);
}
