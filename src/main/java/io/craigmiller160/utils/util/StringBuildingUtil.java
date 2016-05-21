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
 * A class with utility methods to help
 * with building Strings.
 *
 * Created by craigmiller on 4/19/16.
 */
public class StringBuildingUtil {

    /**
     * Determine if the provided String to see if the last
     * character is a space. Useful for concatenating
     * Strings into a proper sentence.
     *
     * @param text the text to check.
     * @return true if the text ends with a space.
     */
    public static boolean endsWithSpace(String text){
        return ensureStringExists(text) && text.endsWith(" ");
    }

    /**
     * Ensure that the provided text ends with a space.
     * If it already ends with a space, or if it's an empty
     * String, this method makes
     * no changes. If it does not, then a space is added.
     *
     * Useful for concatenating Strings into a proper
     * sentence.
     *
     * @param text the text to ensure ends with a space.
     * @return the text with a space at the end.
     */
    public static String ensureEndsWithSpace(String text){
        if(!ensureStringExists(text)){
            return text;
        }

        if(text.endsWith(" ")){
            return text;
        }
        else{
            return text + " ";
        }
    }

    /**
     * Ensure that the text in the provided StringBuilder
     * ends with a space. If it already ends with a space,
     * or if it's an empty String, this method makes
     * no changes. If it does not, then a space is added.
     *
     * Useful for concatenating Strings into a proper
     * sentence.
     *
     * @param builder the StringBuilder containing the text.
     * @return the StringBuilder, with the text ending with a space.
     */
    public static StringBuilder ensureEndsWithSpace(StringBuilder builder){
        if(!ensureStringExists(builder.toString())){
            return builder;
        }

        char lastChar = builder.charAt(builder.length() - 1);
        if(lastChar == ' '){
            return builder;
        }
        else{
            return builder.append(" ");
        }
    }

    /**
     * Ensure that the text in the provided StringBuffer
     * ends with a space. If it already ends with a space,
     * or if it's an empty String this method makes
     * no changes. If it does not, then a space is added.
     *
     * Useful for concatenating Strings into a proper
     * sentence.
     *
     * @param buffer the StringBuffer containing the text.
     * @return the StringBuffer, with the text ending with a space.
     */
    public static StringBuffer ensureEndsWithSpace(StringBuffer buffer){
        if(!ensureStringExists(buffer.toString())){
            return buffer;
        }

        char lastChar = buffer.charAt(buffer.length() - 1);
        if(lastChar == ' '){
            return buffer;
        }
        else{
            return buffer.append(" ");
        }
    }

    /**
     * Tests a String to ensure it's both not null
     * and actually contains text content.
     *
     * @param text the String to test.
     * @return true if the String is not null and
     *              contains text content.
     */
    public static boolean ensureStringExists(String text){
        return text != null && !(text.equals(""));
    }

    /**
     * Ensure the provided text ends with a comma
     * and a space. If it already does, or if it's
     * an empty String, nothing happens. If it
     * ends in a space, a comma is added before it.
     * Otherwise, a comma and a space are added
     * to the end.
     *
     * @param text the text to ensure ends with a comma
     *             and a space.
     * @return the text ending with a comma and a space.
     */
    public static String ensureEndsWithCommaSpace(String text){
        if(!ensureStringExists(text)){
            return text;
        }

        if(text.endsWith(", ")){
            return text;
        }
        else if(text.endsWith(" ")){
            return text.substring(0, text.length() - 1) + ", ";
        }
        else{
            return text + ", ";
        }
    }

    /**
     * Ensure the provided builder's text ends with a comma
     * and a space. If it already does, or if it's
     * an empty String, nothing happens. If it
     * ends in a space, a comma is added before it.
     * Otherwise, a comma and a space are added
     * to the end.
     *
     * @param builder the text to ensure ends with a comma
     *             and a space.
     * @return the builder ending with a comma and a space.
     */
    public static StringBuilder ensureEndsWithCommaSpace(StringBuilder builder){
        if(!ensureStringExists(builder.toString())){
            return builder;
        }

        String ending = builder.substring(builder.length() - 2, builder.length());
        if(ending.equals(", ")){
            return builder;
        }
        else if(ending.endsWith(" ")){
            return new StringBuilder(builder.substring(0, builder.length() - 1) + ", ");
        }
        else{
            return builder.append(", ");
        }
    }

    /**
     * Ensure the provided buffer's text ends with a comma
     * and a space. If it already does, or if it's
     * an empty String, nothing happens. If it
     * ends in a space, a comma is added before it.
     * Otherwise, a comma and a space are added
     * to the end.
     *
     * @param buffer the text to ensure ends with a comma
     *             and a space.
     * @return the buffer ending with a comma and a space.
     */
    public static StringBuffer ensureEndsWithCommaSpace(StringBuffer buffer){
        if(!ensureStringExists(buffer.toString())){
            return buffer;
        }

        String ending = buffer.substring(buffer.length() - 2, buffer.length());
        if(ending.equals(", ")){
            return buffer;
        }
        else if(ending.endsWith(" ")){
            return new StringBuffer(buffer.substring(0, buffer.length() - 1) + ", ");
        }
        else{
            return buffer.append(", ");
        }
    }

}
