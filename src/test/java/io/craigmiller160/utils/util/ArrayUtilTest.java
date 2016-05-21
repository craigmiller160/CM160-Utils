package io.craigmiller160.utils.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A JUnit test class for ArrayUtil.
 *
 * Created by craig on 5/21/16.
 */
public class ArrayUtilTest {

    @Test
    public void testDeepToString(){
        Object[] array = {
                "One",
                "Two",
                new Object[]{
                        "Sub One",
                        "Sub Two"
                },
                "Three"
        };

        String expected = "[One, Two, [Sub One, Sub Two], Three]";

        String result = ArrayUtil.deepToString(array);
        assertNotNull("DeepToString result is null", result);
        assertEquals("DeepToString wrong output", expected, result);
    }

}
