package org.litespring.test.v1;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({ApplicationContextTest.class, BeanFactoryTest.class})
public class V1AllTests {
}
