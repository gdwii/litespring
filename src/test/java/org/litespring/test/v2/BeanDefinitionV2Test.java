package org.litespring.test.v2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.CLassPathResource;

import java.util.List;

public class BeanDefinitionV2Test {
    @Test
    public void testGetBeanDefinition(){
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        reader.loadBeanDefinitions(new CLassPathResource("petstore-v2.xml"));

        BeanDefinition beanDefinition = defaultBeanFactory.getBeanDefinition("petStore");
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        Assertions.assertEquals(propertyValues.size(), 4);

        {
            PropertyValue propertyValue = getPropertyValue(propertyValues, "accountDao");
            Assertions.assertNotNull(propertyValues);
            Assertions.assertTrue(propertyValue.getValue() instanceof RuntimeBeanReference);
        }

        {
            PropertyValue propertyValue = getPropertyValue(propertyValues, "itemDao");
            Assertions.assertNotNull(propertyValues);
            Assertions.assertTrue(propertyValue.getValue() instanceof RuntimeBeanReference);
        }
    }

    private PropertyValue getPropertyValue(List<PropertyValue> propertyValues, String name) {
        for(PropertyValue propertyValue : propertyValues){
            if(propertyValue.getName().equals(name)){
                return propertyValue;
            }
        }
        return null;
    }
}