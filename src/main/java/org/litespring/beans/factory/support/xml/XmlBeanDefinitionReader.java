package org.litespring.beans.factory.support.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.util.ClassUtils;
import org.litespring.core.io.Resource;

import java.io.InputStream;
import java.util.List;

public class XmlBeanDefinitionReader {
    private BeanDefinitionRegistry beanDefinitionRegistry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_CLASS = "class";


    public void loadBeanDefinitions(Resource resource){
        try (InputStream in = resource.getInputStream()){
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);

            List<Element> elementList = document.getRootElement().elements();

            for (Element element : elementList) {
                String id = element.attributeValue(ATTRIBUTE_ID);
                String className = element.attributeValue(ATTRIBUTE_CLASS);
                beanDefinitionRegistry.registerBeanDefinition(id, new GenericBeanDefinition(id, className));
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + resource.getDescription(), e);
        }
    }
}