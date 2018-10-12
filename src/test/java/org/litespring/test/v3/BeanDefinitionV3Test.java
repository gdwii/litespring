package org.litespring.test.v3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgumentValues;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.CLassPathResource;

import java.util.List;
import java.util.Map;

public class BeanDefinitionV3Test {
    private DefaultBeanFactory defaultBeanFactory;
    private XmlBeanDefinitionReader reader;

    @BeforeEach
    public void setUp(){
        defaultBeanFactory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        reader.loadBeanDefinitions(new CLassPathResource("petstore-v3.xml"));
    }

    @Test
    public void testConstructorArgument(){
        ConstructorArgumentValues constructorArgumentValues = getConstructorArgumentValues("petStore", "org.litespring.service.v3.PetStoreService");

        List<ConstructorArgumentValues.ValueHolder> argumentValues = constructorArgumentValues.getGenericArgumentValues();
        Assertions.assertEquals(3, argumentValues.size());
        RuntimeBeanReference ref1 = (RuntimeBeanReference)argumentValues.get(0).getValue();
        Assertions.assertEquals("accountDao", ref1.getBeanName());
        RuntimeBeanReference ref2 = (RuntimeBeanReference)argumentValues.get(1).getValue();
        Assertions.assertEquals("itemDao", ref2.getBeanName());
        TypedStringValue strValue = (TypedStringValue)argumentValues.get(2).getValue();
        Assertions.assertEquals( "1", strValue.getValue());

        Assertions.assertTrue(constructorArgumentValues.getIndexedArgumentValues().isEmpty());
    }

    @Test
    public void testConstructorIndexArgument(){
        ConstructorArgumentValues constructorArgumentValues = getConstructorArgumentValues("indexConstructorPetStore", "org.litespring.service.v3.MultiConstructorPetStoreService");
        Map<Integer, ConstructorArgumentValues.ValueHolder> indexedArgumentValues = constructorArgumentValues.getIndexedArgumentValues();

        Assertions.assertEquals(2, indexedArgumentValues.size());
        RuntimeBeanReference ref1 = (RuntimeBeanReference)indexedArgumentValues.get(0).getValue();
        Assertions.assertEquals("v1PetStoreDao", ref1.getBeanName());
        RuntimeBeanReference ref2 = (RuntimeBeanReference)indexedArgumentValues.get(1).getValue();
        Assertions.assertEquals("v2PetStoreDao", ref2.getBeanName());

        Assertions.assertTrue(constructorArgumentValues.getGenericArgumentValues().isEmpty());
    }

    @Test
    public void testConstructorNameArgument(){
        ConstructorArgumentValues constructorArgumentValues = getConstructorArgumentValues("nameConstructorPetStore", "org.litespring.service.v3.MultiConstructorPetStoreService");

        List<ConstructorArgumentValues.ValueHolder> argumentValues = constructorArgumentValues.getGenericArgumentValues();
        Assertions.assertEquals(2, argumentValues.size());

        RuntimeBeanReference ref1 = (RuntimeBeanReference)argumentValues.get(1).getValue();
        Assertions.assertEquals("v1PetStoreDao", ref1.getBeanName());
        Assertions.assertEquals("v1", argumentValues.get(1).getName());

        RuntimeBeanReference ref2 = (RuntimeBeanReference)argumentValues.get(0).getValue();
        Assertions.assertEquals("v2PetStoreDao", ref2.getBeanName());
        Assertions.assertEquals("v2", argumentValues.get(0).getName());

        Assertions.assertTrue(constructorArgumentValues.getIndexedArgumentValues().isEmpty());
    }

    @Test
    public void testConstructorTypeArgument(){
        ConstructorArgumentValues constructorArgumentValues = getConstructorArgumentValues("typeConstructorPetStore", "org.litespring.service.v3.MultiConstructorPetStoreService");

        List<ConstructorArgumentValues.ValueHolder> argumentValues = constructorArgumentValues.getGenericArgumentValues();
        Assertions.assertEquals(2, argumentValues.size());

        TypedStringValue strValue1 = (TypedStringValue)argumentValues.get(0).getValue();
        Assertions.assertEquals("13", strValue1.getValue());
        Assertions.assertEquals("java.lang.String", argumentValues.get(0).getType());

        TypedStringValue strValue2 = (TypedStringValue)argumentValues.get(1).getValue();
        Assertions.assertEquals("12", strValue2.getValue());
        Assertions.assertEquals("int", argumentValues.get(1).getType());

        Assertions.assertTrue(constructorArgumentValues.getIndexedArgumentValues().isEmpty());
    }

    private ConstructorArgumentValues getConstructorArgumentValues(String beanId, String beanClassName) {
        BeanDefinition beanDefinition = defaultBeanFactory.getBeanDefinition(beanId);
        Assertions.assertEquals(beanClassName, beanDefinition.getBeanClassName());
        return beanDefinition.getConstructorArgumentValues();
    }
}