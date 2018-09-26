package org.litespring.test.v1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.service.v1.PetStoreService;

public class BeanFactoryTest {
    @Test
    public void testGetBean(){
        BeanFactory beanFactory = new DefaultBeanFactory("petstore-v1.xml");
        BeanDefinition beanDefine = beanFactory.getBeanDefine("petStore");
        Assertions.assertNotNull(beanDefine);
        Assertions.assertEquals("org.litespring.service.v1.PetStoreService", beanDefine.getBeanClassName());

        PetStoreService petStoreService = (PetStoreService)beanFactory.getBean("petStore");
        Assertions.assertNotNull(petStoreService);
    }

    @Test
    public void testInvalidBean(){
        BeanFactory beanFactory = new DefaultBeanFactory("petstore-v1.xml");
        Assertions.assertThrows(BeanCreationException.class, () -> beanFactory.getBean("invalidBean"));
    }

    @Test
    public void testInvalidXml(){
        Assertions.assertThrows(BeanDefinitionStoreException.class, () -> new DefaultBeanFactory("xxx.xml"));
    }
}
