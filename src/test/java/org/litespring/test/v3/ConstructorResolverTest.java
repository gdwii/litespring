package org.litespring.test.v3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.support.ConstructorResolver;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.CLassPathResource;
import org.litespring.service.v3.PetStoreService;

public class ConstructorResolverTest {
    @Test
    public void testGetBeanDefinition(){
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        reader.loadBeanDefinitions(new CLassPathResource("petstore-v3.xml"));
        BeanDefinition beanDefinition = defaultBeanFactory.getBeanDefinition("petStore");

        ConstructorResolver constructorResolver = new ConstructorResolver(defaultBeanFactory);
        PetStoreService petStore = (PetStoreService)constructorResolver.autowireConstructor(beanDefinition);

        Assertions.assertNotNull(petStore.getAccountDao());
        Assertions.assertNotNull(petStore.getItemDao());
        Assertions.assertEquals(1, petStore.getVersion());
    }
}