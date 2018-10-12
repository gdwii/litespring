package org.litespring.test.v3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.support.ConstructorResolver;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.CLassPathResource;
import org.litespring.service.v3.MultiConstructorPetStoreService;
import org.litespring.service.v3.PetStoreService;

public class ConstructorResolverTest {
    private DefaultBeanFactory defaultBeanFactory;
    private XmlBeanDefinitionReader reader;

    @BeforeEach
    public void setUp(){
        defaultBeanFactory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        reader.loadBeanDefinitions(new CLassPathResource("petstore-v3.xml"));
    }

    @Test
    public void testAutowireConstructor(){
        PetStoreService petStore = getBean("petStore");
        Assertions.assertNotNull(petStore.getAccountDao());
        Assertions.assertNotNull(petStore.getItemDao());
        Assertions.assertEquals(1, petStore.getVersion());
    }

    @Test
    public void testAutowireConstructorWithIndex(){
        MultiConstructorPetStoreService petStore = getBean("indexConstructorPetStore");
        Assertions.assertEquals(1, petStore.getV1().getVersion());
        Assertions.assertEquals(2, petStore.getV2().getVersion());
    }

    @Test
    public void testAutowireConstructorWithType(){
        MultiConstructorPetStoreService petStore = getBean("typeConstructorPetStore");
        Assertions.assertEquals("13", petStore.getName());
        Assertions.assertEquals(12, petStore.getNum());
    }

    @Test
    public void testAutowireConstructorWithName(){
        MultiConstructorPetStoreService petStore = getBean("nameConstructorPetStore");
        Assertions.assertEquals(1, petStore.getV1().getVersion());
        Assertions.assertEquals(2, petStore.getV2().getVersion());
    }

    private <T> T getBean(String beanId) {
        BeanDefinition beanDefinition = defaultBeanFactory.getBeanDefinition(beanId);
        ConstructorResolver constructorResolver = new ConstructorResolver(defaultBeanFactory);
        return (T)constructorResolver.autowireConstructor(beanDefinition);
    }
}