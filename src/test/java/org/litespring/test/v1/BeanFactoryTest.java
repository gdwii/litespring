package org.litespring.test.v1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.support.xml.XmlBeanDefinitionReader;
import org.litespring.service.v1.PetStoreService;

public class BeanFactoryTest {
    private DefaultBeanFactory defaultBeanFactory;
    private XmlBeanDefinitionReader reader;

    @BeforeEach
    public void setUp(){
        defaultBeanFactory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(defaultBeanFactory);
    }

    @Test
    public void testGetBean(){
        reader.loadBeanDefinitions("petstore-v1.xml");

        BeanDefinition beanDefine = defaultBeanFactory.getBeanDefinition("petStore");
        Assertions.assertNotNull(beanDefine);
        Assertions.assertEquals("org.litespring.service.v1.PetStoreService", beanDefine.getBeanClassName());

        PetStoreService petStoreService = (PetStoreService)defaultBeanFactory.getBean("petStore");
        Assertions.assertNotNull(petStoreService);
    }

    @Test
    public void testInvalidBean(){
        reader.loadBeanDefinitions("petstore-v1.xml");

        Assertions.assertThrows(BeanCreationException.class, () -> defaultBeanFactory.getBean("invalidBean"));
    }

    @Test
    public void testInvalidXml(){
        Assertions.assertThrows(BeanDefinitionStoreException.class, () -> reader.loadBeanDefinitions("xxx.xml"));
    }
}
