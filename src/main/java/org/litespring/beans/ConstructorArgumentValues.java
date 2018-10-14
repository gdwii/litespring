package org.litespring.beans;

import org.litespring.util.Assert;
import org.litespring.util.ClassUtils;
import org.litespring.util.StringUtils;

import java.util.*;

public class ConstructorArgumentValues {
    private final List<ValueHolder> genericArgumentValues = new ArrayList<>();

    private final Map<Integer, ValueHolder> indexedArgumentValues = new HashMap<>();

    public List<ValueHolder> getGenericArgumentValues() {
        return Collections.unmodifiableList(genericArgumentValues);
    }

    public void addGenericArgumentValue(ValueHolder newValue) {
        Assert.notNull(newValue, "ValueHolder must not be null");
        if (!this.genericArgumentValues.contains(newValue)) {
            genericArgumentValues.add(newValue);
        }
    }

    public void addIndexArgumentValue(int index, ValueHolder newValue) {
        Assert.isTrue(index >= 0, "Index must not be negative");
        Assert.notNull(newValue, "ValueHolder must not be null");
        indexedArgumentValues.put(index, newValue);
    }

    public Map<Integer, ValueHolder> getIndexedArgumentValues() {
        return indexedArgumentValues;
    }

    public boolean hasIndexedArgumentValue(int index) {
        return indexedArgumentValues.containsKey(index);
    }

    public int getArgumentCount(){
        return genericArgumentValues.size() + indexedArgumentValues.size();
    }

    public boolean isEmpty() {
        return genericArgumentValues.isEmpty() && indexedArgumentValues.isEmpty();
    }

    public ValueHolder getArgumentValue(int index, Class<?> requiredType, String requiredName, Set<ValueHolder> usedValueHolders) {
        Assert.isTrue(index >= 0, "Index must not be negative");
        ValueHolder valueHolder = getIndexedArgumentValue(index, requiredType, requiredName);
        if(valueHolder == null){
            valueHolder = getGenericArgumentValue(requiredType, requiredName, usedValueHolders);
        }
        return valueHolder;
    }

    /**
     * Get argument value for the given index in the constructor argument list.
     * @param index the index in the constructor argument list
     * @param requiredType the type to match (can be {@code null} to match
     * untyped values only)
     * @param requiredName the type to match (can be {@code null} to match
     * unnamed values only, or empty String to match any name)
     * @return the ValueHolder for the argument, or {@code null} if none set
     */
    public ValueHolder getIndexedArgumentValue(int index, Class<?> requiredType, String requiredName) {
        Assert.isTrue(index >= 0, "Index must not be negative");
        ValueHolder valueHolder = indexedArgumentValues.get(index);
        if(valueHolder == null){
            return null;
        }

        if(StringUtils.hasText(valueHolder.getName()) && StringUtils.hasText(requiredName) && !valueHolder.getName().equals(requiredName)){
            return null;
        }

        if(StringUtils.hasText(valueHolder.getType()) && requiredType != null && !ClassUtils.matchesTypeName(requiredType, valueHolder.getType())){
            return null;
        }
        return valueHolder;
    }

    /**
     * Look for the next generic argument value that matches the given type,
     * ignoring argument values that have already been used in the current
     * resolution process.
     * @param requiredType the type to match (can be {@code null} to find
     * an arbitrary next generic argument value)
     * @param requiredName the name to match (can be {@code null} to not
     * match argument values by name, or empty String to match any name)
     * @param usedValueHolders a Set of ValueHolder objects that have already been used
     * in the current resolution process and should therefore not be returned again
     * @return the ValueHolder for the argument, or {@code null} if none found
     */
    private ValueHolder getGenericArgumentValue(Class<?> requiredType, String requiredName, Set<ValueHolder> usedValueHolders) {
        for(ValueHolder valueHolder : genericArgumentValues){
            if(usedValueHolders != null && usedValueHolders.contains(valueHolder)){
                continue ;
            }

            if(StringUtils.hasText(valueHolder.getName()) && (!StringUtils.hasText(requiredName) || !valueHolder.getName().equals(requiredName))){
                continue ;
            }

            if(StringUtils.hasText(valueHolder.getType()) && (requiredType == null || !ClassUtils.matchesTypeName(requiredType, valueHolder.getType()))){
                continue ;
            }

            return valueHolder;
        }
        return null;
    }

    public static class ValueHolder {
        private String name;

        private String type;

        private Object value;

        private boolean converted = false;

        private Object convertedValue;

        public ValueHolder(Object value) {
            this.value = value;
        }

        public ValueHolder(String type, Object value) {
            this.type = type;
            this.value = value;
        }

        public ValueHolder(String name, String type, Object value) {
            this.name = name;
            this.type = type;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public boolean isConverted() {
            return converted;
        }

        public void setConverted(boolean converted) {
            this.converted = converted;
        }

        public Object getConvertedValue() {
            return convertedValue;
        }

        public void setConvertedValue(Object convertedValue) {
            this.convertedValue = convertedValue;
        }
    }
}
