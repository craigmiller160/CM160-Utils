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

package io.craigmiller160.utils.collection;

import io.craigmiller160.utils.sample.Val;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test class for the SuperWeakHashMap
 * class.
 *
 * The tests are mainly focused on methods that
 * run the process to clear stale entries.
 *
 * Created by craig on 4/25/16.
 */
public class SuperWeakHashMapTest {

    private SuperWeakHashMap<Val, Val> map;
    private Val key1;
    private Val key2;
    private Val value1;
    private Val value2;

    /**
     * Run before each test.
     */
    @Before
    public void before(){
        map = new SuperWeakHashMap<>();

        key1 = new Val("Key1");
        value1 = new Val("Value1");
        key2 = new Val("Key2");
        value2 = new Val("Value2");

        map.put(key1, value1);
        map.put(key2, value2);
    }

    /**
     * Test the size() method and WeakReference
     * removal in general.
     */
    @Test
    public void testSize(){
        //Test the initial size to catch any weird early errors
        assertEquals("Map size after adding two entries is wrong", 2, map.size());

        //Null a key and force the runtime to do garbage collection
        key2 = null;
        Runtime.getRuntime().gc();

        assertEquals("Map size after nulling a key is wrong", 1, map.size());

        //Null a value and force the runtime to do garbage collection
        value1 = null;
        Runtime.getRuntime().gc();

        assertEquals("Map size after nulling a value is wrong", 0, map.size());
    }

    /**
     * Test the isEmpty() method.
     */
    @Test
    public void testIsEmpty(){
        //Test the initial size to catch any weird early errors
        assertEquals("Map size after adding two entries is wrong", 2, map.size());

        //Null a key and a value and force the runtime to do garbage collection
        key2 = null;
        value1 = null;
        Runtime.getRuntime().gc();

        assertTrue("Map should be empty", map.isEmpty());
    }

    /**
     * Test how the KeySet affects the nulling of entries.
     */
    @Test
    public void testKeySet(){
        //Test the initial size to catch any weird early errors
        assertEquals("Map size after adding two entries is wrong", 2, map.size());

        Set<Val> keySet = map.keySet();

        //Null a key, which shouldn't change the map because the keySet exists
        key2 = null;
        Runtime.getRuntime().gc();

        assertEquals("Map size should not have changed", 2, map.size());

        //Null the keySet, which should change the map's size now
        keySet = null;
        Runtime.getRuntime().gc();

        assertEquals("Map size should have changed now", 1, map.size());
    }

    /**
     * Test removing an item from the map
     */
    @Test
    public void testRemove(){
        map.remove(key1);

        assertEquals("Map size after removal is wrong", 1, map.size());
    }

}
