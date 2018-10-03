package org.litespring.test.v2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.litespring.beans.propertyeditors.CustomBooleanEditor;

import java.beans.PropertyEditor;

public class CustomBooleanEditorTest {
    private PropertyEditor propertyEditor;

    @BeforeEach
    public void setUp(){
        propertyEditor = new CustomBooleanEditor(true);
    }

    @Test
    public void testConvertNormal(){
        propertyEditor.setAsText("true");
        Assertions.assertTrue(((Boolean)propertyEditor.getValue()).booleanValue());
        propertyEditor.setAsText("false");
        Assertions.assertFalse(((Boolean)propertyEditor.getValue()).booleanValue());

        propertyEditor.setAsText("on");
        Assertions.assertTrue(((Boolean)propertyEditor.getValue()).booleanValue());
        propertyEditor.setAsText("off");
        Assertions.assertFalse(((Boolean)propertyEditor.getValue()).booleanValue());

        propertyEditor.setAsText("yes");
        Assertions.assertTrue(((Boolean)propertyEditor.getValue()).booleanValue());
        propertyEditor.setAsText("no");
        Assertions.assertFalse(((Boolean)propertyEditor.getValue()).booleanValue());
    }

    @Test
    public void testEmptyString(){
        propertyEditor.setAsText("");
        Assertions.assertNull(propertyEditor.getValue());
    }

    @Test
    public void testInvalidString(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> propertyEditor.setAsText("ss"));
    }
}
