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

package io.craigmiller160.utils.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * JUnit test class for the StringUtil class.
 *
 * Created by craigmiller on 4/19/16.
 */
public class StringUtilTest {

    /**
     * Test the basic isEmpty() method, which
     * should return true if the String is null
     * or has no characters, but should return
     * false if all characters are whitespace.
     */
    @Test
    public void testIsEmpty(){
        String nullString = null;
        String emptyString = "";
        String whitespaceString = "  ";
        String string = "This is a String";

        assertTrue("Null String not found to be empty", StringUtil.isEmpty(nullString));
        assertTrue("Empty String not found to be empty", StringUtil.isEmpty(emptyString));
        assertFalse("Whitespace String found to be empty", StringUtil.isEmpty(whitespaceString));
        assertFalse("Regular String found to be empty", StringUtil.isEmpty(string));
    }

    /**
     * Test the isEmpty() method with its boolean
     * parameter to exclude whitespace.
     */
    @Test
    public void testIsEmptyWhitespace(){
        String nullString = null;
        String emptyString = "";
        String whitespaceString = "  ";
        String string = "This is a String";

        assertTrue("Null String not found to be empty", StringUtil.isEmpty(nullString, true));
        assertTrue("Empty String not found to be empty", StringUtil.isEmpty(emptyString, true));
        assertTrue("Whitespace String not found to be empty", StringUtil.isEmpty(whitespaceString, true));
        assertFalse("Regular String found to be empty", StringUtil.isEmpty(string, true));
    }

}
