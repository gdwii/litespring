package org.litespring.beans.factory;

public class UnsatisfiedDependencyException extends BeanCreationException{
    public UnsatisfiedDependencyException(String msg) {
        super(msg);
    }

    public UnsatisfiedDependencyException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnsatisfiedDependencyException(String beanName, String msg) {
        super(beanName, msg);
    }

    public UnsatisfiedDependencyException(String beanName, String msg, Throwable cause) {
        super(beanName, msg, cause);
    }
}
