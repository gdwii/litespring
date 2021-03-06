package org.litespring.beans.propertyeditors;

import org.litespring.util.NumberUtils;
import org.litespring.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.NumberFormat;

public class CustomNumberEditor extends PropertyEditorSupport {
    private final Class<? extends Number> numberClass;

    private final NumberFormat numberFormat;

    private final boolean allowEmpty;

    public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty) {
        this(numberClass, null, allowEmpty);
    }

    public CustomNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmpty) {
        if(!Number.class.isAssignableFrom(numberClass)){
            throw new IllegalArgumentException("Property class must be a subclass of Number");
        }
        this.numberClass = numberClass;
        this.numberFormat = numberFormat;
        this.allowEmpty = allowEmpty;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(allowEmpty && !StringUtils.hasText(text)){
            setValue(null);
            return ;
        }

        if(numberFormat != null){
            // Use given NumberFormat for parsing text.
            setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
            return ;
        }

        // Use default valueOf methods for parsing text.
        setValue(NumberUtils.parseNumber(text, this.numberClass));
    }

    @Override
    public void setValue(Object value) {
        if(value instanceof Number){
            super.setValue(NumberUtils.convertNumberToTargetClass((Number) value, numberClass));
        }else {
            super.setValue(value);
        }
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        if(value == null){
            return "";
        }

        if(numberFormat != null){
            return numberFormat.format(value);
        }

        return value.toString();
    }


}
