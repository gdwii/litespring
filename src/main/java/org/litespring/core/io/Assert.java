package org.litespring.core.io;

public class Assert {
    public static void notNull(Object object, String message) {
        if(object == null){
            throw new IllegalArgumentException(message);
        }
    }
}
