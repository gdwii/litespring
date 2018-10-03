package org.litespring.test.v2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.TypeConverter;
import org.litespring.beans.TypeMismatchException;

public class TypeConverterTest {
    @Test
    public void testConvertStringToInteger(){
        TypeConverter typeConverter = new SimpleTypeConverter();
        Assertions.assertEquals(3, typeConverter.convertIfNecessary("3", Integer.class).intValue());
        Assertions.assertThrows(TypeMismatchException.class, () -> typeConverter.convertIfNecessary("3.1", Integer.class));
    }

    @Test
    public void testConvertStringToBoolean(){
        TypeConverter typeConverter = new SimpleTypeConverter();
        Assertions.assertTrue(typeConverter.convertIfNecessary("true", Boolean.class));
        Assertions.assertThrows(TypeMismatchException.class, () -> typeConverter.convertIfNecessary("xx", Boolean.class));
    }
}
