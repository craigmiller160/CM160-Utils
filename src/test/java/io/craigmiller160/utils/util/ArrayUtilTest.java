/*
 * Copyright 2016 Craig Miller
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
