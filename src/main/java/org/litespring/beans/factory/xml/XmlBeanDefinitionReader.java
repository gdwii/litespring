package org.litespring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypeStringValue;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.core.io.Resource;
import org.litespring.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

public class XmlBeanDefinitionReader {
    private static final Logger logger = LoggerFactory.getLogger(XmlBeanDefinitionReader.class);

    private BeanDefinitionRegistry beanDefinitionRegistry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_CLASS = "class";
    private static final String ATTRIBUTE_SCOPE = "scope";
    private static final String ELEMENT_PROPERTY = "property";
    private static final String ATTRIBUTE_PROPERTY_NAME = "name";
    private static final String ATTRIBUTE_PROPERTY_REF = "ref";
    private static final String ATTRIBUTE_PROPERTY_VALUE = "value";


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
                parsePropertyValue(beanDefinition.getPropertyValues(), element.elements(ELEMENT_PROPERTY));

                beanDefinitionRegistry.registerBeanDefinition(id, beanDefinition);
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + resource.getDescription(), e);
        }
    }

    private void parsePropertyValue(List<PropertyValue> propertyValues, List<Element> elements) {
        if(elements == null){
            return;
        }
        elements.forEach(element -> {
            String propertyName = element.attributeValue(ATTRIBUTE_PROPERTY_NAME);
            if (!StringUtils.hasLength(propertyName)) {
                logger.error("Tag 'property' must have a 'name' attribute");
                return;
            }
            PropertyValue propertyValue = parsePropertyValue(element, propertyName);
            propertyValues.add(propertyValue);
        });
    }

    private PropertyValue parsePropertyValue(Element element, String propertyName) {
        PropertyValue propertyValue = new PropertyValue();
        propertyValue.setName(propertyName);
        propertyValue.setValue(parseValueOfPropertyValue(element, propertyName));
        return propertyValue;
    }

    private Object parseValueOfPropertyValue(Element element, String propertyName) {
        String elementName = (propertyName != null) ?
                "<property> element for property '" + propertyName + "'" :
                "<constructor-arg> element";

        boolean hasRef = element.attribute(ATTRIBUTE_PROPERTY_REF) != null;
        if(hasRef){
            String refValue = element.attributeValue(ATTRIBUTE_PROPERTY_REF);
            if(StringUtils.isEmpty(refValue)){
                logger.error(elementName + " contains empty 'ref' attribute");
                return null;
            }
            return new RuntimeBeanReference(refValue);
        }

        boolean hasValue = element.attribute(ATTRIBUTE_PROPERTY_VALUE) != null;
        if(hasValue){
            return new TypeStringValue(element.attributeValue(ATTRIBUTE_PROPERTY_VALUE));
        }

        throw new RuntimeException(elementName + " must specify a ref or value");
    }
}