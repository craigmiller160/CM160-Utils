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

package io.craigmiller160.utils.reflect;

import io.craigmiller160.utils.sample.ModelOne;
import io.craigmiller160.utils.sample.ModelTwo;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test class for the methods in the
 * MethodUtils class.
 *
 * Created by Craig on 2/14/2016.
 */
public class MethodUtilsTest {

    /**
     * Test a simple method with a single argument, and
     * the correct values supplied to MethodUtils.
     */
    @Test
    public void testSimpleMethodSuccess(){
        Method method = getMethod("method1");
        assertNotNull("Method for test wasn't retrieved", method);
        assertNotNull(MethodUtils.validateInvocationAndConvertParams(method, "One"));
    }

    /**
     * Test a method with multiple arguments,
     * and the correct values are all supplied
     * to MethodUtils.
     */
    @Test
    public void testLargerMethodSuccess(){
        Method method = getMethod("method2");
        assertNotNull("Method for test wasn't retrieved", method);
        assertNotNull(MethodUtils.validateInvocationAndConvertParams(method, "One", 12, false));
    }

    /**
     * Test a varargs method, passing a
     * single argument to the varargs parameter.
     * All supplied values are correct.
     */
    @Test
    public void testVarArgsSingleArgSuccess(){
        Method method = getMethod("method3");
        assertNotNull("Method for test wasn't retrieved", method);
        assertNotNull(MethodUtils.validateInvocationAndConvertParams(method, "One", "Two"));
    }

    /**
     * Test a varargs method, passing
     * multiple arguments to the varargs parameter.
     * All supplied values are correct.
     */
    @Test
    public void testVarArgsMultipleArgSuccess(){
        Method method = getMethod("method3");
        assertNotNull("Method for test wasn't retrieved", method);
        assertNotNull(MethodUtils.validateInvocationAndConvertParams(method, "One", "Two", "Three", "Four", "Five"));
    }

    /**
     * Test a varargs method, passing an array to
     * the varargs parameter. All supplied values
     * are correct.
     */
    @Test
    public void testVarArgsWithArraySuccess(){
        Method method = getMethod("method3");
        assertNotNull("Method for test wasn't retrieved", method);

        String[] args = new String[] {"Two", "Three", "Four", "Five"};

        assertNotNull(MethodUtils.validateInvocationAndConvertParams(method, "One", args));
    }

    /**
     * Test a varargs method, but supply no varargs parameters.
     * All values that are supplied are correct.
     */
    @Test
    public void testEmptyVarArgsSuccess(){
        Method method = getMethod("method3");
        assertNotNull("Method for test wasn't retrieved", method);
        assertNotNull(MethodUtils.validateInvocationAndConvertParams(method, "Only"));
    }

    /**
     * Test a simple method with a single argument, but
     * supply arguments that should cause it to fail.
     */
    @Test
    public void testSimpleMethodWrongArg(){
        Method method = getMethod("method1");
        assertNotNull("Method for test wasn't retrieved", method);
        assertNull(MethodUtils.validateInvocationAndConvertParams(method, 22));
    }

    /**
     * Test a method with multiple arguments,
     * but the values supplied are incorrect,
     * causing it to return false.
     */
    @Test
    public void testLargerMethodWrongArgs(){
        Method method = getMethod("method2");
        assertNotNull("Method for test wasn't retrieved", method);
        assertNull(MethodUtils.validateInvocationAndConvertParams(method, 22, "Hi there", "What"));
    }

    /**
     * Test a method with multiple arguments, but
     * too few arguments are supplied, causing it to return false.
     */
    @Test
    public void testLargerMethodTooFewArgs(){
        Method method = getMethod("method2");
        assertNotNull("Method for test wasn't retrieved", method);
        assertNull(MethodUtils.validateInvocationAndConvertParams(method, "Hi there", 22));
    }

    /**
     * Test a method with multiple arguments,
     * but too many arguments are supplied, causing
     * it to fail.
     */
    @Test
    public void testLargerMethodTooManyArgs(){
        Method method = getMethod("method2");
        assertNotNull("Method for test wasn't retrieved", method);
        assertNull(MethodUtils.validateInvocationAndConvertParams(method, "Hi there", 22, false, 22, 22, 22, 22));
    }

    /**
     * Test validating a method with polymorphic values. That
     * is, the values provided are subclasses of what is expected.
     */
    @Test
    public void testPolymorphicValues(){
        Method method = getMethod("method4");
        assertNotNull("Method for test wasn't retrieved", method);
        assertNotNull(MethodUtils.validateInvocationAndConvertParams(method, "Hi there", 22));
    }

    /**
     * Test validating a method with primitive parameters.
     * The primitive params of the method and the ones passed
     * to it are identical.
     */
    @Test
    public void testPrimitivesIdenticalParams(){
        Method method = getMethod("method6");
        assertNotNull("Method for test wasn't retrieved", method);
        assertNotNull(MethodUtils.validateInvocationAndConvertParams(method, 22));
    }

    /**
     * Test validating a method with polymorphic values.
     * That is, th values provided are subclasses of what is
     * expected. This test also tests this with a VarArgs
     * parameter as well.
     */
    @Test
    public void testPolymorphicValuesVarArgs(){
        Method method = getMethod("method5");
        assertNotNull("Method for test wasn't retrieved", method);
        assertNotNull(MethodUtils.validateInvocationAndConvertParams(method, true, 23, 27.56));
    }

    /**
     * Test validating a method with a varArgs
     * that is passed an array that is a subclass
     * of what the method is expecting.
     */
    @Test
    public void testPolymorphicArrayValuesVarArgs(){
        Method method = getMethod("method5");
        assertNotNull("Method for test wasn't retrieved", method);

        Double[] args = new Double[]{23.72, 27.56};

        assertNotNull(MethodUtils.validateInvocationAndConvertParams(method, true, args));
    }

    /**
     * This test pulls three methods and tests them for being
     * duplicates. The first and third should be duplicates, the
     * second is unique.
     *
     * @throws Exception if an error occurs outside the scope
     * of the test.
     */
    @Test
    public void testIsDuplicateMethod() throws Exception{
        Method m1 = ModelOne.class.getMethod("setStringField", String.class);
        Method m2 = ModelTwo.class.getMethod("setFieldThree", String.class);
        Method m3 = ModelTwo.class.getMethod("setStringField", String.class);

        assertTrue("Duplicate methods weren't recognized", MethodUtils.isDuplicateMethod(m1, m3));
        assertFalse("Non-Duplicate methods recognized as duplicate", MethodUtils.isDuplicateMethod(m1, m2));
    }

    /**
     * Simple utility method for the regularly used operation
     * to get the method to test in the test methods.
     *
     * @param methodName the name of the method to retrieve.
     * @return the found test method, or null if none are found.
     */
    private Method getMethod(String methodName){
        TestClass tc = new TestClass();

        Method[] methods = tc.getClass().getMethods();
        for(Method m : methods){
            if(m.getName().equals(methodName)){
                return m;
            }
        }
        return null;
    }

    /**
     * Dummy TestClass to use for testing the reflective methods.
     *
     */
    private class TestClass{

        /**
         * A simple method with a single argument.
         *
         * @param message a String argument.
         * @return the String argument.
         */
        public String method1(String message){
            return message;
        }

        /**
         * A more complex method with multiple
         * arguments.
         *
         * @param message a String argument.
         * @param i an Integer argument.
         * @param b a Boolean argument.
         * @return all three arguments concatenated together.
         */
        public String method2(String message, Integer i, Boolean b){
            return message + " " + i + " " + b;
        }

        public String method3(String message, String...more){
            return message + " " + Arrays.toString(more);
        }

        public String method4(Object o1, Number o2){
            return o1.toString() + " " + o2.toString();
        }

        public String method5(Object o1, Number...nums){
            return o1.toString() + " " + Arrays.toString(nums);
        }

        public String method6(int i){
            return "Param: " + i;
        }

    }

}
