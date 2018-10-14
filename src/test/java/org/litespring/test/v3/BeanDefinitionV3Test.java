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
        assertRuntimeBeanReference(argumentValues.get(0), null, null, "accountDao");
        assertRuntimeBeanReference(argumentValues.get(1), null, null, "itemDao");
        assertTypedStringValue(argumentValues.get(2), null, null, "1");

        Assertions.assertTrue(constructorArgumentValues.getIndexedArgumentValues().isEmpty());
    }

    @Test
    public void testConstructorIndexArgument(){
        ConstructorArgumentValues constructorArgumentValues = getConstructorArgumentValues("indexConstructorPetStore", "org.litespring.service.v3.MultiConstructorPetStoreService");
        Map<Integer, ConstructorArgumentValues.ValueHolder> indexedArgumentValues = constructorArgumentValues.getIndexedArgumentValues();

        Assertions.assertEquals(2, indexedArgumentValues.size());
        assertRuntimeBeanReference(indexedArgumentValues.get(0), null, null, "v1PetStoreDao");
        assertRuntimeBeanReference(indexedArgumentValues.get(1), null, null, "v2PetStoreDao");

        Assertions.assertTrue(constructorArgumentValues.getGenericArgumentValues().isEmpty());
    }

    @Test
    public void testConstructorNameArgument(){
        ConstructorArgumentValues constructorArgumentValues = getConstructorArgumentValues("nameConstructorPetStore", "org.litespring.service.v3.MultiConstructorPetStoreService");

        List<ConstructorArgumentValues.ValueHolder> argumentValues = constructorArgumentValues.getGenericArgumentValues();
        Assertions.assertEquals(2, argumentValues.size());

        assertRuntimeBeanReference(argumentValues.get(0), "v2", null, "v2PetStoreDao");
        assertRuntimeBeanReference(argumentValues.get(1), "v1", null, "v1PetStoreDao");

        Assertions.assertTrue(constructorArgumentValues.getIndexedArgumentValues().isEmpty());
    }

    @Test
    public void testConstructorTypeArgument(){
        ConstructorArgumentValues constructorArgumentValues = getConstructorArgumentValues("typeConstructorPetStore", "org.litespring.service.v3.MultiConstructorPetStoreService");

        List<ConstructorArgumentValues.ValueHolder> argumentValues = constructorArgumentValues.getGenericArgumentValues();
        Assertions.assertEquals(2, argumentValues.size());
        assertTypedStringValue(argumentValues.get(0), null, "java.lang.String", "13");
        assertTypedStringValue(argumentValues.get(1), null, "java.lang.Integer", "12");

        Map<Integer, ConstructorArgumentValues.ValueHolder> indexedArgumentValues = constructorArgumentValues.getIndexedArgumentValues();
        Assertions.assertTrue(constructorArgumentValues.getIndexedArgumentValues().isEmpty());
    }

    @Test
    public void testMixConstructorArgument(){
        ConstructorArgumentValues constructorArgumentValues = getConstructorArgumentValues("mixConstructorPetStore", "org.litespring.service.v3.MultiConstructorPetStoreService");

        List<ConstructorArgumentValues.ValueHolder> argumentValues = constructorArgumentValues.getGenericArgumentValues();
        Assertions.assertEquals(1, argumentValues.size());
        assertTypedStringValue(argumentValues.get(0), "num", "java.lang.Integer", "12");

        Map<Integer, ConstructorArgumentValues.ValueHolder> indexedArgumentValues = constructorArgumentValues.getIndexedArgumentValues();
        Assertions.assertEquals(1, indexedArgumentValues.size());
        assertTypedStringValue(indexedArgumentValues.get(0), "name", "java.lang.String", "13");
    }

    private void assertTypedStringValue(ConstructorArgumentValues.ValueHolder valueHolder, String name, String type, String value){
        assertNameAndType(valueHolder, name, type);

        Assertions.assertTrue(valueHolder.getValue() instanceof TypedStringValue);
        TypedStringValue stringValue = (TypedStringValue)valueHolder.getValue();
        Assertions.assertEquals(value, stringValue.getValue());
    }

    private void assertRuntimeBeanReference(ConstructorArgumentValues.ValueHolder valueHolder, String name, String type, String beanName){
        assertNameAndType(valueHolder, name, type);

        Assertions.assertTrue(valueHolder.getValue() instanceof RuntimeBeanReference);
        RuntimeBeanReference reference = (RuntimeBeanReference)valueHolder.getValue();
        Assertions.assertEquals(beanName, reference.getBeanName());
    }

    private void assertNameAndType(ConstructorArgumentValues.ValueHolder valueHolder, String name, String type) {
        Assertions.assertEquals(name, valueHolder.getName());
        Assertions.assertEquals(type, valueHolder.getType());
    }

    private ConstructorArgumentValues getConstructorArgumentValues(String beanId, String beanClassName) {
        BeanDefinition beanDefinition = defaultBeanFactory.getBeanDefinition(beanId);
        Assertions.assertEquals(beanClassName, beanDefinition.getBeanClassName());
        return beanDefinition.getConstructorArgumentValues();
    }
}