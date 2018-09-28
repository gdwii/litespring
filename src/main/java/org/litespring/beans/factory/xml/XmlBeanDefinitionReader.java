package org.litespring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
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
    private static final String ATTRIBUTE_SCOPE = "scope";


    public void loadBeanDefinitions(Resource resource){
        try (InputStream in = resource.getInputStream()){
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);

            List<Element> elementList = document.getRootElement().elements();

            for (Element element : elementList) {
                String id = element.attributeValue(ATTRIBUTE_ID);
                String className = element.attributeValue(ATTRIBUTE_CLASS);
                BeanDefinition beanDefinition = new GenericBeanDefinition(id, className);
                beanDefinition.setScope(element.attributeValue(ATTRIBUTE_SCOPE));
                beanDefinitionRegistry.registerBeanDefinition(id, beanDefinition);
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + resource.getDescription(), e);
        }
    }
}