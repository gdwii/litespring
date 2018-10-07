package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgumentValues;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.TypeConverter;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.List;

public class ConstructorResolver {
    private static final Logger logger = LoggerFactory.getLogger(ConstructorResolver.class);
    private final BeanFactory beanFactory;
    public ConstructorResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object autowireConstructor(BeanDefinition beanDefinition) {
        Class<?> beanClass = loadBeanClass(beanDefinition);

        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(beanFactory);
        TypeConverter typeConverter = new SimpleTypeConverter();

        Constructor<?> constructorToUse = null;
        Object[] argsToUse = null;

        Constructor<?>[] candidates = beanClass.getConstructors();
        ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();

        for(int i = 0; i < candidates.length; i ++){
            Constructor<?> candidate = candidates[i];
            if(candidate.getParameterCount() != constructorArgumentValues.getArgumentCount()){
                continue ;
            }

            argsToUse = new Object[candidate.getParameterCount()];

            boolean result = valuesMatchType(candidate.getParameterTypes(),
                                            constructorArgumentValues.getGenericArgumentValues(),
                                            argsToUse,
                                            valueResolver,
                                            typeConverter);
            if(result){
                constructorToUse = candidate;
                break;
            }
        }

        return instantiateBean(constructorToUse, argsToUse, beanDefinition);
    }

    private boolean valuesMatchType(Class<?>[] parameterTypes,
                                    List<ConstructorArgumentValues.ValueHolder> genericArgumentValues,
                                    Object[] argsToUse,
                                    BeanDefinitionValueResolver valueResolver,
                                    TypeConverter typeConverter) {
        for(int i = 0; i < parameterTypes.length; i ++){
            try{
                Class<?> parameterType = parameterTypes[i];
                Object parameterValue = genericArgumentValues.get(i).getValue();
                Object resolvedValue = valueResolver.resolveValueIfNecessary(parameterValue);
                Object convertedValue = typeConverter.convertIfNecessary(resolvedValue, parameterType);
                argsToUse[i] = convertedValue;
            } catch (Exception e){
                logger.error("valuesMatchType is not match", e);
                return false;
            }
        }
        return true;
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
}
