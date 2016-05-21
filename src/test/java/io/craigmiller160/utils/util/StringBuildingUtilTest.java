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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by craigmiller on 4/19/16.
 */
public class StringBuildingUtilTest {

    @Test
    public void testEndsWithSpace(){
        String endsWithSpace = "Hello World ";
        String endsWithoutSpace = "Hello World";

        assertTrue(StringBuildingUtil.endsWithSpace(endsWithSpace));
        assertFalse(StringBuildingUtil.endsWithSpace(endsWithoutSpace));
    }

    @Test
    public void testEnsureEndsWithSpaceString(){
        String endsWithSpace = "Hello World ";
        String endsWithoutSpace = "Hello World";

        assertEquals(endsWithSpace,
                StringBuildingUtil.ensureEndsWithSpace(endsWithSpace));
        assertEquals(endsWithSpace,
                StringBuildingUtil.ensureEndsWithSpace(endsWithoutSpace));
    }

    @Test
    public void testEnsureEndsWithSpaceStringBuilder(){
        StringBuilder endsWithSpace = new StringBuilder("Hello World ");
        StringBuilder endsWithoutSpace = new StringBuilder("Hello World");

        assertEquals(endsWithSpace.toString(),
                StringBuildingUtil.ensureEndsWithSpace(endsWithSpace).toString());
        assertEquals(endsWithSpace.toString(),
                StringBuildingUtil.ensureEndsWithSpace(endsWithoutSpace).toString());
    }

    @Test
    public void testEnsureEndsWithSpaceStringBuffer(){
        StringBuffer endsWithSpace = new StringBuffer("Hello World ");
        StringBuffer endsWithoutSpace = new StringBuffer("Hello World");

        assertEquals(endsWithSpace.toString(),
                StringBuildingUtil.ensureEndsWithSpace(endsWithSpace).toString());
        assertEquals(endsWithSpace.toString(),
                StringBuildingUtil.ensureEndsWithSpace(endsWithoutSpace).toString());
    }

    /**
     * The method being tested here is used by
     * many of the other methods in StringUtil.
     * If this test fails, it is potentially
     * the cause of other test failures.
     */
    @Test
    public void testEnsureStringExists(){
        String exists = "Hello World";
        String emptyString = "";

        assertTrue(StringBuildingUtil.ensureStringExists(exists));
        assertFalse(StringBuildingUtil.ensureStringExists(null));
        assertFalse(StringBuildingUtil.ensureStringExists(emptyString));
    }

    @Test
    public void testEnsureEndsWithCommaSpaceString(){
        String endsWithCommaSpace = "Hello World, ";
        String endsWithSpace = "Hello World ";
        String noEndingChars = "Hello World";

        assertEquals(endsWithCommaSpace,
                StringBuildingUtil.ensureEndsWithCommaSpace(endsWithCommaSpace));
        assertEquals(endsWithCommaSpace,
                StringBuildingUtil.ensureEndsWithCommaSpace(endsWithSpace));
        assertEquals(endsWithCommaSpace,
                StringBuildingUtil.ensureEndsWithCommaSpace(noEndingChars));
    }

    @Test
    public void testEnsureEndsWithCommaSpaceStringBuilder(){
        StringBuilder endsWithCommaSpace = new StringBuilder("Hello World, ");
        StringBuilder endsWithSpace = new StringBuilder("Hello World ");
        StringBuilder noEndingChars = new StringBuilder("Hello World");

        assertEquals(endsWithCommaSpace.toString(),
                StringBuildingUtil.ensureEndsWithCommaSpace(endsWithCommaSpace).toString());
        assertEquals(endsWithCommaSpace.toString(),
                StringBuildingUtil.ensureEndsWithCommaSpace(endsWithSpace).toString());
        assertEquals(endsWithCommaSpace.toString(),
                StringBuildingUtil.ensureEndsWithCommaSpace(noEndingChars).toString());
    }

    @Test
    public void testEnsureEndsWithCommaSpaceStringBuffer(){
        StringBuffer endsWithCommaSpace = new StringBuffer("Hello World, ");
        StringBuffer endsWithSpace = new StringBuffer("Hello World ");
        StringBuffer noEndingChars = new StringBuffer("Hello World");

        assertEquals(endsWithCommaSpace.toString(),
                StringBuildingUtil.ensureEndsWithCommaSpace(endsWithCommaSpace).toString());
        assertEquals(endsWithCommaSpace.toString(),
                StringBuildingUtil.ensureEndsWithCommaSpace(endsWithSpace).toString());
        assertEquals(endsWithCommaSpace.toString(),
                StringBuildingUtil.ensureEndsWithCommaSpace(noEndingChars).toString());
    }

}
