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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

    /**
     * Test validateAndConvert method.
     *
     * VarArgs: Yes
     * Count: actualParamCount > expectedTypeCount
     * Types: All valid
     * Result: Successful validation and conversion
     */
    @Test
    public void testVAC_VarArgs_MoreActualParams_ValidTypes(){
        Class<?>[] expectedTypes = {String.class, Integer[].class};
        Object[] actualParams = {"Foo", 1, 2, 3, 4};

        //Get the values and check that they passed validation
        Object[] finalParams = ParamUtils.validateInvocationAndConvertParams(expectedTypes, true, actualParams);
        assertNotNull("finalParams were returned null, validation failed", finalParams);
        assertEquals("finalParams is the wrong size", 2, finalParams.length);

        //Check how the validation and conversion returned the values
        assertEquals("finalParams[0] is not of type String", String.class, finalParams[0].getClass());
        assertEquals("finalParams[0] has the wrong value", "Foo", finalParams[0]);
        assertEquals("finalParams[1] is not of type Integer[]", Integer[].class, finalParams[1].getClass());
        Integer[] varArgs = (Integer[]) finalParams[1];
        assertEquals("finalParams varArgs array is the wrong size", 4, varArgs.length);
        assertEquals("finalParams varArgs[0] is the wrong value", (Integer) 1, varArgs[0]);
        assertEquals("finalParams varArgs[0] is the wrong value", (Integer) 2, varArgs[1]);
        assertEquals("finalParams varArgs[0] is the wrong value", (Integer) 3, varArgs[2]);
        assertEquals("finalParams varArgs[0] is the wrong value", (Integer) 4, varArgs[3]);
    }

    /**
     * Test validateAndConvert method.
     *
     * VarArgs: Yes
     * Count: actualParamCount == expectedTypeCount
     * Types: All valid
     * Result: Successful validation and conversion
     */
    @Test
    public void testVAC_VarArgs_EqualCounts_ValidTypes(){
        Class<?>[] expectedTypes = {String.class, Integer[].class};
        Object[] actualParams = {"Foo", 1};

        //Get the values and check that they passed validation
        Object[] finalParams = ParamUtils.validateInvocationAndConvertParams(expectedTypes, true, actualParams);
        assertNotNull("finalParams were returned null, validation failed", finalParams);
        assertEquals("finalParams is the wrong size", 2, finalParams.length);

        //Check how the validation and conversion returned the values
        assertEquals("finalParams[0] is not of type String", String.class, finalParams[0].getClass());
        assertEquals("finalParams[0] has the wrong value", "Foo", finalParams[0]);
        assertEquals("finalParams[1] is not of type Integer[]", Integer[].class, finalParams[1].getClass());
        Integer[] varArgs = (Integer[]) finalParams[1];
        assertEquals("finalParams varArgs array is the wrong size", 1, varArgs.length);
        assertEquals("finalParams varArgs[0] is the wrong value", (Integer) 1, varArgs[0]);
    }

    /**
     * Test validateAndConvert method.
     *
     * VarArgs: Yes
     * Count: actualParamCount == expectedTypeCount
     * Types: All valid, array is provided for varargs
     * Result: Successful validation and conversion
     */
    @Test
    public void testVAC_VarArgs_EqualTypes_ValidTypes_ArrayParam(){
        Class<?>[] expectedTypes = {String.class, Integer[].class};
        Object[] actualParams = {"Foo", new Integer[]{1, 2, 3, 4}};

        //Get the values and check that they passed validation
        Object[] finalParams = ParamUtils.validateInvocationAndConvertParams(expectedTypes, true, actualParams);
        assertNotNull("finalParams were returned null, validation failed", finalParams);
        assertEquals("finalParams is the wrong size", 2, finalParams.length);

        //Check how the validation and conversion returned the values
        assertEquals("finalParams[0] is not of type String", String.class, finalParams[0].getClass());
        assertEquals("finalParams[0] has the wrong value", "Foo", finalParams[0]);
        assertEquals("finalParams[1] is not of type Integer[]", Integer[].class, finalParams[1].getClass());
        Integer[] varArgs = (Integer[]) finalParams[1];
        assertEquals("finalParams varArgs array is the wrong size", 4, varArgs.length);
        assertEquals("finalParams varArgs[0] is the wrong value", (Integer) 1, varArgs[0]);
        assertEquals("finalParams varArgs[0] is the wrong value", (Integer) 2, varArgs[1]);
        assertEquals("finalParams varArgs[0] is the wrong value", (Integer) 3, varArgs[2]);
        assertEquals("finalParams varArgs[0] is the wrong value", (Integer) 4, varArgs[3]);
    }

    /**
     * Test validateAndConvert method.
     *
     * VarArgs: Yes
     * Count: actualParamCount == expectedTypeCount
     * Types: All valid, array is provided for varargs
     * Uses Primitives
     * Result: Successful validation and conversion
     */
    @Test
    public void testVAC_EqualTypes_ValidTypes_ArrayParam_Primitive(){
        Class<?>[] expectedTypes = {String.class, int[].class};
        Object[] actualParams = {"Foo", new int[]{1, 2, 3, 4}};

        //Get the values and check that they passed validation
        Object[] finalParams = ParamUtils.validateInvocationAndConvertParams(expectedTypes, true, actualParams);
        assertNotNull("finalParams were returned null, validation failed", finalParams);
        assertEquals("finalParams is the wrong size", 2, finalParams.length);

        //Check how the validation and conversion returned the values
        assertEquals("finalParams[0] is not of type String", String.class, finalParams[0].getClass());
        assertEquals("finalParams[0] has the wrong value", "Foo", finalParams[0]);
        assertEquals("finalParams[1] is not of type Integer[]", int[].class, finalParams[1].getClass());
        int[] varArgs = (int[]) finalParams[1];
        assertEquals("finalParams varArgs array is the wrong size", 4, varArgs.length);
        assertEquals("finalParams varArgs[0] is the wrong value", (int) 1, varArgs[0]);
        assertEquals("finalParams varArgs[0] is the wrong value", (int) 2, varArgs[1]);
        assertEquals("finalParams varArgs[0] is the wrong value", (int) 3, varArgs[2]);
        assertEquals("finalParams varArgs[0] is the wrong value", (int) 4, varArgs[3]);
    }

    /**
     * Test validateAndConvert method.
     *
     * VarArgs: No
     * Count: actualParamCount == expectedTypeCount
     * Types: All valid
     * Result: Successful validation and conversion
     */
    @Test
    public void testVAC_EqualCount_ValidTypes(){
        Class<?>[] expectedTypes = {String.class, Integer.class};
        Object[] actualParams = {"Foo", 1};

        //Get the values and check that they passed validation
        Object[] finalParams = ParamUtils.validateInvocationAndConvertParams(expectedTypes, false, actualParams);
        assertNotNull("finalParams were returned null, validation failed", finalParams);
        assertEquals("finalParams is the wrong size", 2, finalParams.length);

        //Check how the validation and conversion returned the values
        assertEquals("finalParams[0] is not of type String", String.class, finalParams[0].getClass());
        assertEquals("finalParams[0] has the wrong value", "Foo", finalParams[0]);
        assertEquals("finalParams[1] is not of type Integer[]", Integer.class, finalParams[1].getClass());
        assertEquals("finalParams[1] has the wrong value", 1, finalParams[1]);
    }

    /**
     * Test validateAndConvert method.
     *
     * VarArgs: Yes
     * Count: actualParamCount == expectedTypeCount - 1
     * Types: All valid
     * Result: Successful validation and conversion
     */
    @Test
    public void testVAC_VarArgs_MoreExpectedTypes_ValidTypes(){
        Class<?>[] expectedTypes = {String.class, Integer[].class};
        Object[] actualParams = {"Foo"};

        //Get the values and check that they passed validation
        Object[] finalParams = ParamUtils.validateInvocationAndConvertParams(expectedTypes, true, actualParams);
        assertNotNull("finalParams were returned null, validation failed", finalParams);
        assertEquals("finalParams is the wrong size", 2, finalParams.length);

        //Check how the validation and conversion returned the values
        assertEquals("finalParams[0] is not of type String", String.class, finalParams[0].getClass());
        assertEquals("finalParams[0] has the wrong value", "Foo", finalParams[0]);
        assertEquals("finalParams[1] is not of type Integer[]", Integer[].class, finalParams[1].getClass());
        Integer[] varArgs = (Integer[]) finalParams[1];
        assertEquals("finalParams varArgs array is not empty", 0, varArgs.length);
    }

    /**
     * Test validateAndConvert method.
     *
     * VarArgs: Yes
     * Count: actualParamCount == 0 && expectedTypeCount == 1
     * Types: All valid
     * Result: Successful validation and conversion
     */
    @Test
    public void testVAC_VarArgs_OneExpectedNoActual_ValidTypes(){
        Class<?>[] expectedTypes = {String[].class};

        //Get the values and check that they passed validation
        Object[] finalParams = ParamUtils.validateInvocationAndConvertParams(expectedTypes, true);
        assertNotNull("finalParams were returned null, validation failed", finalParams);
        assertEquals("finalParams is the wrong size", 1, finalParams.length);

        //Check how the validation and conversion returned the values
        assertEquals("finalParams[0] is not of type String", String[].class, finalParams[0].getClass());
        String[] varArgs = (String[]) finalParams[0];
        assertEquals("finalParams varArgs array is not empty", 0, varArgs.length);
    }

    /**
     * Test validateAndConvert method.
     *
     * VarArgs: No
     * Count: actualParamCount == 0 && expectedTypeCount == 0
     * Types: No parameters, no types
     * Result: Successful validation and conversion
     */
    @Test
    public void testVAC_NoParams(){
        Class<?>[] expectedTypes = {};

        //Get the values and check that they passed validation
        Object[] finalParams = ParamUtils.validateInvocationAndConvertParams(expectedTypes, false);
        assertNotNull("finalParams were returned null, validation failed", finalParams);
        assertEquals("finalParams is the wrong size", 0, finalParams.length);
    }

    /**
     * Test validateAndConvert method.
     *
     * VarArgs: Yes
     * Count: actualParamCount > expectedTypeCount
     * Types: VarArgs position types in actualParams invalid
     * Result: Failed validation and conversion
     */
    @Test
    public void testVAC_VarArgs_MoreActualParams_InvalidTypes(){
        Class<?>[] expectedTypes = {String.class, Integer[].class};
        Object[] actualParams = {"Foo", 1, 2, "Cheese", 4};

        //Get the values and check that they failed validation
        Object[] finalParams = ParamUtils.validateInvocationAndConvertParams(expectedTypes, true, actualParams);
        assertNull("finalParams were not returned null, validation succeeded when it should've failed", finalParams);
    }

    /**
     * Test validateAndConvert method.
     *
     * VarArgs: No
     * Count: actualParamCount == expectedTypeCount
     * Types: Invalid types
     * Result: Failed validation and conversion
     */
    @Test
    public void testVAC_EqualCounts_InvalidTypes(){
        Class<?>[] expectedTypes = {String.class, Integer.class};
        Object[] actualParams = {"Foo", "Cheese"};

        //Get the values and check that they failed validation
        Object[] finalParams = ParamUtils.validateInvocationAndConvertParams(expectedTypes, false, actualParams);
        assertNull("finalParams were not returned null, validation succeeded when it should've failed", finalParams);
    }

    /**
     * Test validateAndConvert method.
     *
     * VarArgs: No
     * Count: actualParamCount > expectedTypeCount
     * Types: All matching parameter positions valid
     * Result: Failed validation and conversion
     */
    @Test
    public void testVAC_MoreActualParams(){
        Class<?>[] expectedTypes = {String.class, Integer.class};
        Object[] actualParams = {"Foo", 1, 2, 3, "Cheese"};

        //Get the values and check that they failed validation
        Object[] finalParams = ParamUtils.validateInvocationAndConvertParams(expectedTypes, false, actualParams);
        assertNull("finalParams were not returned null, validation succeeded when it should've failed", finalParams);
    }

    /**
     * Test validateAndConvert method.
     *
     * VarArgs: No
     * Count: actualParamCount < expectedTypeCount
     * Types: All matching parameter positions valid
     * Result: Failed validation and conversion
     */
    @Test
    public void testVAC_MoreExpectedTypes(){
        Class<?>[] expectedTypes = {String.class, Integer.class};
        Object[] actualParams = {"Foo"};

        //Get the values and check that they failed validation
        Object[] finalParams = ParamUtils.validateInvocationAndConvertParams(expectedTypes, false, actualParams);
        assertNull("finalParams were not returned null, validation succeeded when it should've failed", finalParams);
    }

}
