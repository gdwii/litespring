package org.litespring.beans.factory.support;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory implements BeanFactory {
    private final Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();
    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_CLASS = "class";

    public DefaultBeanFactory(String configFile) {
        loadBeanDefinition(configFile);
    }

    private void loadBeanDefinition(String configFile) {
        try (InputStream in = ClassUtils.getDefaultClassLoader().getResourceAsStream(configFile)){
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);

            List<Element> elementList = document.getRootElement().elements();

            for (Element element : elementList) {
                String id = element.attributeValue(ATTRIBUTE_ID);
                String className = element.attributeValue(ATTRIBUTE_CLASS);

                beanDefinitions.put(id, new GenericBeanDefinition(id, className));
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + configFile, e);
        }
    }

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
}
