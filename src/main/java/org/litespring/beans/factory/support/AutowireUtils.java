package org.litespring.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;

abstract class AutowireUtils {
    public static void sortConstructors(Constructor<?>[] candidates) {
        Arrays.sort(candidates, (c1, c2) ->{
            boolean isC1Public = Modifier.isPublic(c1.getModifiers());
            boolean isC2Public = Modifier.isPublic(c2.getModifiers());
            if(isC1Public != isC2Public){
                return isC1Public ? -1 : 1;
            }

            int c1ParameterCount = c1.getParameterCount();
            int c2ParameterCount = c2.getParameterCount();

            return Integer.compare(c1ParameterCount, c2ParameterCount);
        });
    }
}
