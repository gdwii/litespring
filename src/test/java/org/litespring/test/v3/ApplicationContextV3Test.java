package org.litespring.test.v3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.dao.v2.AccountDao;
import org.litespring.dao.v2.ItemDao;
import org.litespring.service.v3.PetStoreService;


public class ApplicationContextV3Test {
    @Test
    public void testGetBeanProperty(){
        ApplicationContext context = new ClassPathXmlApplicationContext("petstore-v3.xml");
        PetStoreService petStoreService = (PetStoreService) context.getBean("petStore");
        Assertions.assertTrue(petStoreService.getAccountDao() instanceof AccountDao);
        Assertions.assertTrue(petStoreService.getItemDao() instanceof ItemDao);
        Assertions.assertEquals(1, petStoreService.getVersion());
    }
}
