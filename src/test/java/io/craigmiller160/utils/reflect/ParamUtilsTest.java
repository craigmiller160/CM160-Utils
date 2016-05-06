/*
 * Copyright 2016 Craig Miller
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.craigmiller160.utils.reflect;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test class for the ParamUtils class.
 *
 * Created by craig on 5/5/16.
 */
public class ParamUtilsTest {

    @Test
    public void testIsAcceptablePrimitive() throws Exception{
        boolean result = ParamUtils.isAcceptablePrimitive(int.class, int.class);
        assertTrue("The primitive int was able to accept the other primitive int", result);

        result = ParamUtils.isAcceptablePrimitive(int.class, Integer.class);
        assertTrue("The primitive int was able to accept the wrapper Integer", result);

        result = ParamUtils.isAcceptablePrimitive(Integer.class, int.class);
        assertTrue("The wrapper Integer was able to accept the primitive int", result);

        result = ParamUtils.isAcceptablePrimitive(int.class, Boolean.class);
        assertFalse("The primitive int was able to accept the wrapper Boolean", result);

        result = ParamUtils.isAcceptablePrimitive(long.class, int.class);
        assertTrue("The primitive long wouldn't accept the primitive int", result);
    }

}
