package org.litespring.context.support;

import org.litespring.core.io.CLassPathResource;
import org.litespring.core.io.Resource;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {
    public ClassPathXmlApplicationContext(String configFile) {
        super(configFile);
    }

    @Override
    protected Resource getResourceByPath(String configFile) {
        return new CLassPathResource(configFile, getBeanClassLoader());
    }
}
