package org.litespring.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * {@link ParameterNameDiscoverer} implementation which uses JDK 8's reflection facilities
 * for introspecting parameter names (based on the "-parameters" compiler flag).
 */
public class StandardReflectionParameterNameDiscoverer implements ParameterNameDiscoverer{
    @Override
    public String[] getParameterNames(Constructor<?> ctor) {
        Parameter[] parameters = ctor.getParameters();
        String[] parameterNames = new String[parameters.length];
        for(int i = 0; i < parameters.length; i ++){
            Parameter parameter = parameters[i];
            if(!parameter.isNamePresent()){
                return null;
            }
            parameterNames[i] = parameter.getName();
        }
        return parameterNames;
    }
}
