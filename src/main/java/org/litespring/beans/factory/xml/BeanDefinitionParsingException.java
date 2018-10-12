package org.litespring.beans.factory.xml;

import org.litespring.beans.factory.BeanDefinitionStoreException;

public class BeanDefinitionParsingException extends BeanDefinitionStoreException {
    public BeanDefinitionParsingException(String msg) {
        super(msg);
    }

    public BeanDefinitionParsingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
