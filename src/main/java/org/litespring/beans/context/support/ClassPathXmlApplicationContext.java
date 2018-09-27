package org.litespring.beans.context.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.context.ApplicationContext;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.support.xml.XmlBeanDefinitionReader;

public class ClassPathXmlApplicationContext implements ApplicationContext {
    private DefaultBeanFactory defaultBeanFactory;

    public ClassPathXmlApplicationContext(String configFile) {
        defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        reader.loadBeanDefinitions(configFile);
    }

    @Override
    public Object getBean(String beanId) {
        return defaultBeanFactory.getBean(beanId);
    }
}
