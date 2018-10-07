package org.litespring.test.v3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgumentValues;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.CLassPathResource;

import java.util.List;

public class BeanDefinitionTestV3 {
    @Test
    public void testGetBeanDefinition(){
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        reader.loadBeanDefinitions(new CLassPathResource("petstore-v3.xml"));

        BeanDefinition beanDefinition = defaultBeanFactory.getBeanDefinition("petStore");

        Assertions.assertEquals("org.litespring.service.v3.PetStoreService", beanDefinition.getBeanClassName());

        ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
        List<ConstructorArgumentValues.ValueHolder> argumentValues = constructorArgumentValues.getGenericArgumentValues();

        Assertions.assertEquals(3, argumentValues.size());

        RuntimeBeanReference ref1 = (RuntimeBeanReference)argumentValues.get(0).getValue();
        Assertions.assertEquals("accountDao", ref1.getBeanName());
        RuntimeBeanReference ref2 = (RuntimeBeanReference)argumentValues.get(1).getValue();
        Assertions.assertEquals("itemDao", ref2.getBeanName());

        TypedStringValue strValue = (TypedStringValue)argumentValues.get(2).getValue();
        Assertions.assertEquals( "1", strValue.getValue());
    }
}