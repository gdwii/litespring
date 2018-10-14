package org.litespring.beans.factory.support;

import org.litespring.beans.*;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.UnsatisfiedDependencyException;
import org.litespring.core.ParameterNameDiscoverer;
import org.litespring.core.StandardReflectionParameterNameDiscoverer;
import org.litespring.util.ClassUtils;
import org.litespring.util.MethodInvoker;
import org.litespring.util.ObjectUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConstructorResolver {
    private final BeanFactory beanFactory;

    private ParameterNameDiscoverer parameterNameDiscoverer = new StandardReflectionParameterNameDiscoverer();

    public ConstructorResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object autowireConstructor(BeanDefinition beanDefinition) {
        Class<?> beanClass = loadBeanClass(beanDefinition);

        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(beanFactory);
        TypeConverter typeConverter = new SimpleTypeConverter();

        Constructor<?> constructorToUse = null;
        Object[] argsToUse = null;
        int minTypeDiffWeight = Integer.MAX_VALUE;

        Constructor<?>[] candidates = beanClass.getConstructors();
        AutowireUtils.sortConstructors(candidates);

        ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();

        for(Constructor<?> candidate : candidates){
            if(constructorToUse != null && argsToUse.length > candidate.getParameterCount()){
                // Already found greedy constructor that can be satisfied ->
                // do not look any further, there are only less greedy constructors left.
                break ;
            }

            if(candidate.getParameterCount() != constructorArgumentValues.getArgumentCount()){
                continue ;
            }
            ArgumentsHolder argsHolder;
            try{
                String[] paramNames = parameterNameDiscoverer.getParameterNames(candidate);
                argsHolder = createArgumentArray(constructorArgumentValues, candidate.getParameterTypes(), paramNames, valueResolver, typeConverter);
            }catch (UnsatisfiedDependencyException ex) {
                continue;
            }

            int typeDiffWeight = argsHolder.getTypeDifferenceWeight(candidate.getParameterTypes());
            if(typeDiffWeight < minTypeDiffWeight){
                minTypeDiffWeight = typeDiffWeight;
                constructorToUse = candidate;
                argsToUse = argsHolder.arguments;
            }else if(typeDiffWeight == minTypeDiffWeight && constructorToUse != null){
                throw new BeanCreationException(
                        "Ambiguous constructor matches found in bean '" + beanDefinition.getBeanClassName() + "' " +
                                "(hint: specify index/type/name arguments for simple parameters to avoid type ambiguities): " +
                                Arrays.asList(constructorToUse, candidate));
            }
        }

        return instantiateBean(constructorToUse, argsToUse, beanDefinition);
    }

    private ArgumentsHolder createArgumentArray(ConstructorArgumentValues constructorArgumentValues,
                                                Class<?>[] parameterTypes, String[] paramNames,
                                                BeanDefinitionValueResolver valueResolver, TypeConverter typeConverter) {
        ArgumentsHolder argsHolder = new ArgumentsHolder(parameterTypes.length);
        Set<ConstructorArgumentValues.ValueHolder> usedValueHolders = new HashSet<>(parameterTypes.length);

        for(int paramIndex = 0; paramIndex < parameterTypes.length; paramIndex ++){
            Class<?> parameterType = parameterTypes[paramIndex];
            String paramName = paramNames == null ? "" : paramNames[paramIndex];

            ConstructorArgumentValues.ValueHolder valueHolder = constructorArgumentValues.getArgumentValue(paramIndex, parameterType, paramName, usedValueHolders);
            if(valueHolder == null){
                throw new UnsatisfiedDependencyException(
                        "Ambiguous argument values for parameter of type [" + parameterType.getName() +
                                "] - did you specify the correct bean references as arguments?");
            }
            usedValueHolders.add(valueHolder);

            Object originalValue = valueHolder.getValue();
            Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);
            argsHolder.rawArguments[paramIndex] = resolvedValue;

            Object convertedValue;
            try{
                convertedValue = typeConverter.convertIfNecessary(resolvedValue, parameterTypes[paramIndex]);
            }catch (TypeMismatchException ex) {
                throw new UnsatisfiedDependencyException(
                        "Could not convert argument value of type [" +
                                ObjectUtils.nullSafeClassName(valueHolder.getValue()) +
                                "] to required type [" + parameterType.getName() + "]: " + ex.getMessage());
            }
            argsHolder.arguments[paramIndex] = convertedValue;
        }

        return argsHolder;
    }

    private Class<?> loadBeanClass(BeanDefinition beanDefinition) {
        try {
            return ClassUtils.getDefaultClassLoader().loadClass(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException(beanDefinition.getId(), "Instantiation of bean failed, can't resolve class", e);
        }
    }

    private Object instantiateBean(Constructor<?> constructorToUse, Object[] argsToUse, BeanDefinition beanDefinition) {
        if(constructorToUse == null){
            throw new BeanCreationException(beanDefinition.getId(), "can't find a appropriate constructor");
        }
        try {
            return constructorToUse.newInstance(argsToUse);
        } catch (Exception e) {
            throw new BeanCreationException(beanDefinition.getId(), "can't create instance using " + constructorToUse);
        }
    }

    private static class ArgumentsHolder{
        public final Object[] rawArguments;

        public final Object[] arguments;

        public ArgumentsHolder(int length) {
            arguments = new Object[length];
            rawArguments = new Object[length];
        }

        public int getTypeDifferenceWeight(Class<?>[] parameterTypes) {
            // If valid arguments found, determine type difference weight.
            // Try type difference weight on both the converted arguments and
            // the raw arguments. If the raw weight is better, use it.
            // Decrease raw weight by 1024 to prefer it over equal converted weight.
            int typeDiffWeight = MethodInvoker.getTypeDifferenceWeight(parameterTypes, arguments);
            int rawTypeDiffWeight = MethodInvoker.getTypeDifferenceWeight(parameterTypes, rawArguments) - 1024;
            return Math.min(typeDiffWeight, rawTypeDiffWeight);
        }
    }
}
