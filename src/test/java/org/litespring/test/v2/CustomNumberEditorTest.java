package org.litespring.test.v2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.litespring.beans.propertyeditors.CustomNumberEditor;

import java.beans.PropertyEditor;

public class CustomNumberEditorTest {
    private PropertyEditor propertyEditor;

    @BeforeEach
    public void setUp(){
        propertyEditor = new CustomNumberEditor(Integer.class, true);
    }

    @Test
    public void testConvertNormal(){
        propertyEditor.setAsText("3");
        Assertions.assertEquals(3, ((Integer) propertyEditor.getValue()).intValue());
    }

    @Test
    public void testEmptyString(){
        propertyEditor.setAsText("");
        Assertions.assertNull(propertyEditor.getValue());
    }

    @Test
    public void testInvalidString(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> propertyEditor.setAsText("3.1"));
    }
}
