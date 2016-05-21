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

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test class for the FindAndInvoke
 * class.
 *
 * Created by Craig on 2/14/2016.
 */
public class FindAndInvokeTest {

    @Test
    public void testFindAndInvoke() throws Exception {
        Object[] objects = getObjects();
        Object result = FindAndInvoke.findInvokeOneMethod("method1", objects, "One", "Two");
        assertNotNull("No result returned", result);
        assertEquals("Result wrong type", String.class, result.getClass());
        assertEquals("Result value is wrong", "One Two", result);
    }

    /**
     * Test FindAndInvoke when passing a collection of
     * ObjectAndMethod objects.
     *
     * @throws Exception if unable to execute the operation.
     */
    @Test
    public void testFindAndInvokeWithOAM() throws Exception{
        Collection<ObjectAndMethod> oams = getOams();
        Object result = FindAndInvoke.findInvokeOneMethod(oams, "One", 2);
        assertNotNull("No result returned", result);
        assertEquals("Result wrong type", String.class, result.getClass());
        assertEquals("Result value is wrong", "One 2", result);
    }

    /**
     * Test FindAndInvoke for invoking multiple methods
     * in a single invocation.
     *
     * @throws Exception if an exception occurs while preparing the process.
     */
    @Test
    public void testFindAndInvokeMultiple() throws Exception{
        Object[] objects = getObjects();
        TestClass3 tc3 = new TestClass3();
        for(Object o : objects){
            ((ParentTestClass) o).setTc3(tc3);
        }

        FindAndInvoke.findInvokeAllMethods("method3", objects, "One");

        assertTrue("TestClass1 method wasn't invoked", tc3.getTc1Success());
        assertTrue("TestClass2 method wasn't invoked", tc3.getTc2Success());
    }

    private Collection<ObjectAndMethod> getOams() throws Exception{
        List<ObjectAndMethod> oams = new ArrayList<>();
        TestClass1 tc1 = new TestClass1();
        Method m = tc1.getClass().getMethod("method1", String.class, String.class);
        oams.add(new ObjectAndMethod(tc1, m));

        Method m3 = tc1.getClass().getMethod("method3", String.class);
        oams.add(new ObjectAndMethod(tc1, m3));

        TestClass2 tc2 = new TestClass2();
        Method m2 = tc2.getClass().getMethod("method2", String.class, Integer.class);
        oams.add(new ObjectAndMethod(tc2, m2));

        m3 = tc2.getClass().getMethod("method3", String.class);
        oams.add(new ObjectAndMethod(tc2, m3));

        return oams;
    }

    /**
     * Utility method for getting
     * the group of objects to
     * use in the tests.
     *
     * @return the objects for the tests.
     */
    private Object[] getObjects(){
        Object[] arr = new Object[2];

        arr[0] = new TestClass1();
        arr[1] = new TestClass2();

        return arr;
    }

    private class ParentTestClass{

        protected TestClass3 tc3;

        public void setTc3(TestClass3 tc3){
            this.tc3 = tc3;
        }


    }

    private class TestClass1 extends ParentTestClass{

        public String method1(String s1, String s2){
            return s1 + " " + s2;
        }

        public void method3(String s1){
            if(tc3 != null){
                tc3.setTc1Success(true);
            }
        }

    }

    private class TestClass2 extends ParentTestClass{

        public String method2(String s1, Integer i1){
            return s1 + " " + i1;
        }

        public void method3(String s1){
            if(tc3 != null){
                tc3.setTc2Success(true);
            }
        }

    }

    private class TestClass3{

        private boolean tc1Success = false;
        private boolean tc2Success = false;

        public void setTc1Success(boolean tc1Success){
            this.tc1Success = tc1Success;
        }

        public void setTc2Success(boolean tc2Success){
            this.tc2Success = tc2Success;
        }

        public boolean getTc1Success(){
            return tc1Success;
        }

        public boolean getTc2Success(){
            return tc2Success;
        }

    }

}
