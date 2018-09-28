package org.litespring.test.v1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.context.support.FileSystemXmlApplicationContext;
import org.litespring.service.v1.PetStoreService;

public class ApplicationContextTest {
    @Test
    public void testGetBean(){
        ApplicationContext context = new ClassPathXmlApplicationContext("petstore-v1.xml");
        PetStoreService petStoreService = (PetStoreService)context.getBean("petStore");
        Assertions.assertNotNull(petStoreService);
    }

    @Test
    public void testGetBeanFromFileSystemContext(){
        String path = ResourceTest.class.getClassLoader().getResource("petstore-v1.xml").getPath();
        ApplicationContext context = new FileSystemXmlApplicationContext(path);
        PetStoreService petStoreService = (PetStoreService)context.getBean("petStore");
        Assertions.assertNotNull(petStoreService);
    }
}
