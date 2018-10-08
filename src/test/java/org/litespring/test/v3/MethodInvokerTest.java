package org.litespring.test.v3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.litespring.util.MethodInvoker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MethodInvokerTest {
    @Test
    public void testGetTypeDifferenceWeight(){
        int weight = MethodInvoker.getTypeDifferenceWeight(new Class<?>[]{String.class}, new Object[]{"test"});
        Assertions.assertEquals(0, weight);

        weight = MethodInvoker.getTypeDifferenceWeight(new Class<?>[]{String.class}, new Object[]{new Object()});
        Assertions.assertEquals(Integer.MAX_VALUE, weight);

        weight = MethodInvoker.getTypeDifferenceWeight(new Class<?>[]{int.class}, new Object[]{Integer.MAX_VALUE});
        Assertions.assertEquals(0, weight);

        weight = MethodInvoker.getTypeDifferenceWeight(new Class<?>[]{int[].class}, new Object[]{new int[0]});
        Assertions.assertEquals(0, weight);

        weight = MethodInvoker.getTypeDifferenceWeight(new Class<?>[]{Object[].class}, new Object[]{new int[0]});
        Assertions.assertEquals(Integer.MAX_VALUE, weight);

        weight = MethodInvoker.getTypeDifferenceWeight(new Class<?>[]{Object[].class}, new Object[]{new String[0]});
        Assertions.assertEquals(0, weight);

        weight = MethodInvoker.getTypeDifferenceWeight(new Class<?>[]{int[].class}, new Object[]{new String[0]});
        Assertions.assertEquals(Integer.MAX_VALUE, weight);

        weight = MethodInvoker.getTypeDifferenceWeight(new Class<?>[]{List.class}, new Object[]{new ArrayList<>()});
        Assertions.assertEquals(3, weight);

        weight = MethodInvoker.getTypeDifferenceWeight(new Class<?>[]{Collection.class}, new Object[]{new ArrayList<>()});
        Assertions.assertEquals(5, weight);
    }

}
