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

/**
 * A class containing helper methods for interacting
 * with Strings.
 *
 * Created by craigmiller on 4/19/16.
 */
public class StringUtil {

    /**
     * Test if the String is empty. This also returns
     * true if the String is null.
     *
     * @param string the String to test.
     * @return true if the String is empty.
     */
    public static boolean isEmpty(String string){
        return isEmpty(string, false);
    }

    /**
     * Test if the String is empty. This also returns
     * true if the String is null, and has a boolean'
     * argument to exclude whitespace from counting as
     * String content.
     *
     * @param string the String to test.
     * @param excludeWhitespace if true, a String will be considered
     *                          empty if all it has is whitespace.
     * @return true if the String is empty.
     */
    public static boolean isEmpty(String string, boolean excludeWhitespace){
        return string == null || (excludeWhitespace ? string.trim().isEmpty() : string.isEmpty());
    }

}
