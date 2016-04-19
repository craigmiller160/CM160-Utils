/*
 * Copyright {yyyy} Craig Miller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.craigmiller160.utils.reflect;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Craig on 2/14/2016.
 */
public class FindAndInvokeTest {

    @Test
    public void testFindAndInvoke() throws Exception {
        Object[] objects = getObjects();
        Object result = FindAndInvoke.findAndInvokeMethod(objects, "method1", "One", "Two");
        assertNotNull("No result returned", result);
        assertEquals("Result wrong type", result.getClass(), String.class);
        assertEquals("Result value is wrong", result, "One Two");
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

    private class TestClass1{

        public String method1(String s1, String s2){
            return s1 + " " + s2;
        }

    }

    private class TestClass2{

        public String method2(String s1, Integer i1){
            return s1 + " " + i1;
        }

    }

}
