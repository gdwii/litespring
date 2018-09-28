package org.litespring.context.support;

import org.litespring.context.ApplicationContext;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.support.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.Resource;

public abstract class AbstractApplicationContext implements ApplicationContext {
    private DefaultBeanFactory defaultBeanFactory;

    public AbstractApplicationContext(String configFile) {
        defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);

        Resource resource = getResourceByPath(configFile);
        reader.loadBeanDefinitions(resource);
    }

     protected abstract Resource getResourceByPath(String path);

    @Override
    public Object getBean(String beanId) {
        return defaultBeanFactory.getBean(beanId);
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return defaultBeanFactory.getBeanClassLoader();
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.defaultBeanFactory.setBeanClassLoader(classLoader);
    }
}
