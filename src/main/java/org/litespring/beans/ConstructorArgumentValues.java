package org.litespring.beans;

import org.litespring.util.Assert;

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
        return genericArgumentValues.size();
    }

    public boolean isEmpty() {
        return genericArgumentValues.isEmpty();
    }

    public ValueHolder getArgumentValue(int paramIndex) {
        return genericArgumentValues.get(paramIndex);
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
