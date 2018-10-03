package org.litespring.test.v2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.core.io.CLassPathResource;
import org.litespring.dao.v2.AccountDao;
import org.litespring.dao.v2.ItemDao;
import org.litespring.service.v2.PetStoreService;

import java.util.List;

public class ApplicationContextTestV2 {
    @Test
    public void testGetBeanProperty(){
        ApplicationContext context = new ClassPathXmlApplicationContext("petstore-v2.xml");
        PetStoreService petStoreService = (PetStoreService) context.getBean("petStore");
        Assertions.assertTrue(petStoreService.getAccountDao() instanceof AccountDao);
        Assertions.assertTrue(petStoreService.getItemDao() instanceof ItemDao);
        Assertions.assertEquals("gdwii", petStoreService.getOwner());
        Assertions.assertEquals(1, petStoreService.getVersion());
    }
}