package org.litespring.test.v2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinitionValueResolver;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.CLassPathResource;
import org.litespring.dao.v2.AccountDao;

public class BeanDefinitionValueResolverTest {
    private BeanDefinitionValueResolver resolver;

    @BeforeEach
    public void setUp(){
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        reader.loadBeanDefinitions(new CLassPathResource("petstore-v2.xml"));

        resolver = new BeanDefinitionValueResolver(defaultBeanFactory);
    }

    @Test
    public void testRuntimeBeanReference(){
        RuntimeBeanReference beanReference = new RuntimeBeanReference("accountDao");
        Object beanReferenceValue = resolver.resolve(beanReference);
        Assertions.assertTrue(beanReferenceValue instanceof AccountDao);
    }

    @Test
    public void testTypedStringValue(){
        TypedStringValue stringValue = new TypedStringValue("test");
        Object value = resolver.resolve(stringValue);
        Assertions.assertEquals("test", value);
    }
}