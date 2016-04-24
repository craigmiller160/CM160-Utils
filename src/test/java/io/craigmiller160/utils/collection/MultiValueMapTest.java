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

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test class for the MultiValueMap
 * class.
 *
 * Created by Craig on 2/11/2016.
 */
public class MultiValueMapTest {

    private MultiValueMap<String,String> map;

    private static final String KEY_ONE = "KeyOne";
    private static final String KEY_TWO = "KeyTwo";
    private static final String VALUE_ONE_ONE = "Value11";
    private static final String VALUE_ONE_TWO = "Value12";
    private static final String VALUE_ONE_THREE = "Value13";
    private static final String VALUE_ONE_FOUR = "Value14";
    private static final String VALUE_ONE_FIVE = "Value15";
    private static final String VALUE_TWO_ONE = "Value21";
    private static final String VALUE_TWO_TWO = "Value22";
    private static final String VALUE_TWO_THREE = "Value23";
    private static final String VALUE_TWO_FOUR = "Value24";
    private static final String VALUE_TWO_FIVE = "Value25";

    @Before
    public void before(){
        map = new MultiValueMap<>();
    }

    /**
     * Test the putValue() method, and test every "passive" method
     * to ensure that all internal changes occurred properly.
     */
    @Test
    public void testPutValue(){
        map.putValue(KEY_ONE, VALUE_ONE_ONE);
        map.putValue(KEY_ONE, VALUE_ONE_TWO);

        assertEquals("Map size is wrong", 1, map.size());
        assertEquals("Map fullSize is wrong", 2, map.fullSize());

        assertTrue(String.format("Map does not contain key %s", KEY_ONE), map.containsKey(KEY_ONE));

        Collection<String> keyOneValues = map.get(KEY_ONE);
        assertNotNull(String.format("%s values collection is null", KEY_ONE), keyOneValues);
        assertEquals(String.format("%s values collection is the wrong size", KEY_ONE), 2, keyOneValues.size());
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_ONE, VALUE_ONE_ONE), keyOneValues.contains(VALUE_ONE_ONE));
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_ONE, VALUE_ONE_TWO), keyOneValues.contains(VALUE_ONE_TWO));

        assertTrue(String.format("containsValue() method did not return true for %s", VALUE_ONE_ONE), map.containsValue(VALUE_ONE_ONE));
        assertTrue(String.format("containsValue() method did not return true for %s", VALUE_ONE_TWO), map.containsValue(VALUE_ONE_TWO));

        assertTrue(String.format("containsCollection() method did not return true for %s", KEY_ONE), map.containsCollection(keyOneValues));

        Collection<String> allValues = map.allValues();
        assertNotNull(String.format("AllValues collection is null"), allValues);
        assertEquals("AllValues collection is wrong size", 2, allValues.size());
        assertTrue(String.format("AllValues collection doesn't contain %s", VALUE_ONE_ONE), allValues.contains(VALUE_ONE_ONE));
        assertTrue(String.format("AllValues collection doesn't contain %s", VALUE_ONE_TWO), allValues.contains(VALUE_ONE_TWO));
    }

    /**
     * Test the put() method,
     */
    @Test
    public void testPut(){
        List<String> list = new ArrayList<>();
        list.add(VALUE_ONE_ONE);
        list.add(VALUE_ONE_TWO);
        map.put(KEY_ONE, list);

        assertEquals("Map size is wrong", 1, map.size());
        assertEquals("Map fullSize is wrong", 2, map.fullSize());

        assertTrue(String.format("Map does not contain key %s", KEY_ONE), map.containsKey(KEY_ONE));

        Collection<String> keyOneValues = map.get(KEY_ONE);
        assertNotNull(String.format("%s values collection is null", KEY_ONE), keyOneValues);
        assertEquals(String.format("%s values collection is the wrong size", KEY_ONE), 2, keyOneValues.size());
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_ONE, VALUE_ONE_ONE), keyOneValues.contains(VALUE_ONE_ONE));
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_ONE, VALUE_ONE_TWO), keyOneValues.contains(VALUE_ONE_TWO));

        assertTrue(String.format("containsValue() method did not return true for %s", VALUE_ONE_ONE), map.containsValue(VALUE_ONE_ONE));
        assertTrue(String.format("containsValue() method did not return true for %s", VALUE_ONE_TWO), map.containsValue(VALUE_ONE_TWO));

        assertTrue(String.format("containsCollection() method did not return true for %s", KEY_ONE), map.containsCollection(keyOneValues));

        Collection<String> allValues = map.allValues();
        assertNotNull(String.format("AllValues collection is null"), allValues);
        assertEquals("AllValues collection is wrong size", 2, allValues.size());
        assertTrue(String.format("AllValues collection doesn't contain %s", VALUE_ONE_ONE), allValues.contains(VALUE_ONE_ONE));
        assertTrue(String.format("AllValues collection doesn't contain %s", VALUE_ONE_TWO), allValues.contains(VALUE_ONE_TWO));
    }

    /**
     * Test the putValueInMultipleCollections() method.
     */
    @Test
    public void testPutValueInMultipleCollections(){
        List<String> keys = new ArrayList<>();
        keys.add(KEY_ONE);
        keys.add(KEY_TWO);
        map.putValueInMultipleCollections(keys, VALUE_ONE_ONE);

        assertEquals("Map size is wrong", 2, map.size());
        assertEquals("Map fullSize is wrong", 2, map.fullSize());

        assertTrue(String.format("Map does not contain key %s", KEY_ONE), map.containsKey(KEY_ONE));
        assertTrue(String.format("Map does not contain key %s", KEY_TWO), map.containsKey(KEY_TWO));

        Collection<String> keyOneValues = map.get(KEY_ONE);
        assertNotNull(String.format("%s values collection is null", KEY_ONE), keyOneValues);
        assertEquals(String.format("%s values collection is the wrong size", KEY_ONE), 1, keyOneValues.size());
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_ONE, VALUE_ONE_ONE), keyOneValues.contains(VALUE_ONE_ONE));

        Collection<String> keyTwoValues = map.get(KEY_TWO);
        assertNotNull(String.format("%s values collection is null", KEY_TWO), keyTwoValues);
        assertEquals(String.format("%s values collection is the wrong size", KEY_TWO), 1, keyTwoValues.size());
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_TWO, VALUE_ONE_ONE), keyTwoValues.contains(VALUE_ONE_ONE));

        assertTrue(String.format("containsValue() method did not return true for %s", VALUE_ONE_ONE), map.containsValue(VALUE_ONE_ONE));

        assertTrue(String.format("containsCollection() method did not return true for %s", KEY_ONE), map.containsCollection(keyOneValues));
        assertTrue(String.format("containsCollection() method did not return true for %s", KEY_TWO), map.containsCollection(keyTwoValues));

        Collection<String> allValues = map.allValues();
        assertNotNull(String.format("AllValues collection is null"), allValues);
        assertEquals("AllValues collection is wrong size", 1, allValues.size());
        assertTrue(String.format("AllValues collection doesn't contain %s", VALUE_ONE_ONE), allValues.contains(VALUE_ONE_ONE));
    }

    /**
     * Test the putMultipleValuesIntoCollection() method.
     */
    @Test
    public void testPutMultipleValuesIntoCollection(){
        List<String> values = new ArrayList<>();
        values.add(VALUE_ONE_ONE);
        values.add(VALUE_ONE_TWO);

        map.putMultipleValuesIntoCollection(KEY_ONE, values);

        assertEquals("Map size is wrong", 1, map.size());
        assertEquals("Map fullSize is wrong", 2, map.fullSize());

        assertTrue(String.format("Map does not contain key %s", KEY_ONE), map.containsKey(KEY_ONE));

        Collection<String> keyOneValues = map.get(KEY_ONE);
        assertNotNull(String.format("%s values collection is null", KEY_ONE), keyOneValues);
        assertEquals(String.format("%s values collection is the wrong size", KEY_ONE), 2, keyOneValues.size());
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_ONE, VALUE_ONE_ONE), keyOneValues.contains(VALUE_ONE_ONE));
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_ONE, VALUE_ONE_TWO), keyOneValues.contains(VALUE_ONE_TWO));

        assertTrue(String.format("containsValue() method did not return true for %s", VALUE_ONE_ONE), map.containsValue(VALUE_ONE_ONE));
        assertTrue(String.format("containsValue() method did not return true for %s", VALUE_ONE_TWO), map.containsValue(VALUE_ONE_TWO));

        assertTrue(String.format("containsCollection() method did not return true for %s", KEY_ONE), map.containsCollection(keyOneValues));

        Collection<String> allValues = map.allValues();
        assertNotNull(String.format("AllValues collection is null"), allValues);
        assertEquals("AllValues collection is wrong size", 2, allValues.size());
        assertTrue(String.format("AllValues collection doesn't contain %s", VALUE_ONE_ONE), allValues.contains(VALUE_ONE_ONE));
        assertTrue(String.format("AllValues collection doesn't contain %s", VALUE_ONE_TWO), allValues.contains(VALUE_ONE_TWO));
    }

    /**
     * Test the remove() method.
     */
    @Test
    public void testRemove(){
        map.putValue(KEY_ONE, VALUE_ONE_ONE);
        map.putValue(KEY_ONE, VALUE_ONE_TWO);
        map.putValue(KEY_TWO, VALUE_TWO_ONE);
        map.putValue(KEY_TWO, VALUE_TWO_TWO);

        //One test for how it currently is to ensure that everything is added.
        assertEquals("Map fullSize pre-remove is wrong", 4, map.fullSize());

        map.remove(KEY_TWO);

        assertEquals("Map size is wrong", 1, map.size());
        assertEquals("Map fullSize is wrong", 2, map.fullSize());

        assertTrue(String.format("Map does not contain key %s", KEY_ONE), map.containsKey(KEY_ONE));

        Collection<String> keyOneValues = map.get(KEY_ONE);
        assertNotNull(String.format("%s values collection is null", KEY_ONE), keyOneValues);
        assertEquals(String.format("%s values collection is the wrong size", KEY_ONE), 2, keyOneValues.size());
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_ONE, VALUE_ONE_ONE), keyOneValues.contains(VALUE_ONE_ONE));
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_ONE, VALUE_ONE_TWO), keyOneValues.contains(VALUE_ONE_TWO));

        assertTrue(String.format("containsValue() method did not return true for %s", VALUE_ONE_ONE), map.containsValue(VALUE_ONE_ONE));
        assertTrue(String.format("containsValue() method did not return true for %s", VALUE_ONE_TWO), map.containsValue(VALUE_ONE_TWO));

        assertTrue(String.format("containsCollection() method did not return true for %s", KEY_ONE), map.containsCollection(keyOneValues));

        Collection<String> allValues = map.allValues();
        assertNotNull(String.format("AllValues collection is null"), allValues);
        assertEquals("AllValues collection is wrong size", 2, allValues.size());
        assertTrue(String.format("AllValues collection doesn't contain %s", VALUE_ONE_ONE), allValues.contains(VALUE_ONE_ONE));
        assertTrue(String.format("AllValues collection doesn't contain %s", VALUE_ONE_TWO), allValues.contains(VALUE_ONE_TWO));
    }

    /**
     * Test the removeValue() method.
     */
    @Test
    public void testRemoveValue(){
        map.putValue(KEY_ONE, VALUE_ONE_ONE);
        map.putValue(KEY_ONE, VALUE_ONE_TWO);
        map.putValue(KEY_TWO, VALUE_TWO_ONE);

        //One test for how it currently is to ensure that everything is added.
        assertEquals("Map fullSize pre-remove is wrong", 3, map.fullSize());

        map.removeValue(VALUE_TWO_ONE);

        assertEquals("Map size is wrong", 1, map.size());
        assertEquals("Map fullSize is wrong", 2, map.fullSize());

        assertTrue(String.format("Map does not contain key %s", KEY_ONE), map.containsKey(KEY_ONE));

        Collection<String> keyOneValues = map.get(KEY_ONE);
        assertNotNull(String.format("%s values collection is null", KEY_ONE), keyOneValues);
        assertEquals(String.format("%s values collection is the wrong size", KEY_ONE), 2, keyOneValues.size());
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_ONE, VALUE_ONE_ONE), keyOneValues.contains(VALUE_ONE_ONE));
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_ONE, VALUE_ONE_TWO), keyOneValues.contains(VALUE_ONE_TWO));

        assertTrue(String.format("containsValue() method did not return true for %s", VALUE_ONE_ONE), map.containsValue(VALUE_ONE_ONE));
        assertTrue(String.format("containsValue() method did not return true for %s", VALUE_ONE_TWO), map.containsValue(VALUE_ONE_TWO));

        assertTrue(String.format("containsCollection() method did not return true for %s", KEY_ONE), map.containsCollection(keyOneValues));

        Collection<String> allValues = map.allValues();
        assertNotNull(String.format("AllValues collection is null"), allValues);
        assertEquals("AllValues collection is wrong size", 2, allValues.size());
        assertTrue(String.format("AllValues collection doesn't contain %s", VALUE_ONE_ONE), allValues.contains(VALUE_ONE_ONE));
        assertTrue(String.format("AllValues collection doesn't contain %s", VALUE_ONE_TWO), allValues.contains(VALUE_ONE_TWO));
    }

    /**
     * Test the putAll() method.
     */
    @Test
    public void testPutAll(){
        //Use a set to test using an alternate collection type from the internal default
        Map<String,Set<String>> m = new HashMap<>();
        Set<String> values = new HashSet<>();
        values.add(VALUE_ONE_ONE);
        values.add(VALUE_ONE_TWO);
        m.put(KEY_ONE, values);
        map.putAll(m);

        assertEquals("Map size is wrong", 1, map.size());
        assertEquals("Map fullSize is wrong", 2, map.fullSize());

        assertTrue(String.format("Map does not contain key %s", KEY_ONE), map.containsKey(KEY_ONE));

        Collection<String> keyOneValues = map.get(KEY_ONE);
        assertNotNull(String.format("%s values collection is null", KEY_ONE), keyOneValues);
        assertEquals(String.format("%s values collection is the wrong size", KEY_ONE), 2, keyOneValues.size());
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_ONE, VALUE_ONE_ONE), keyOneValues.contains(VALUE_ONE_ONE));
        assertTrue(String.format("%1$s values collection does not contain %2$s", KEY_ONE, VALUE_ONE_TWO), keyOneValues.contains(VALUE_ONE_TWO));

        assertTrue(String.format("containsValue() method did not return true for %s", VALUE_ONE_ONE), map.containsValue(VALUE_ONE_ONE));
        assertTrue(String.format("containsValue() method did not return true for %s", VALUE_ONE_TWO), map.containsValue(VALUE_ONE_TWO));

        assertTrue(String.format("containsCollection() method did not return true for %s", KEY_ONE), map.containsCollection(keyOneValues));

        Collection<String> allValues = map.allValues();
        assertNotNull(String.format("AllValues collection is null"), allValues);
        assertEquals("AllValues collection is wrong size", 2, allValues.size());
        assertTrue(String.format("AllValues collection doesn't contain %s", VALUE_ONE_ONE), allValues.contains(VALUE_ONE_ONE));
        assertTrue(String.format("AllValues collection doesn't contain %s", VALUE_ONE_TWO), allValues.contains(VALUE_ONE_TWO));
    }

    /**
     * Test the clear() method.
     */
    @Test
    public void testClear(){
        map.putValue(KEY_ONE, VALUE_ONE_ONE);
        map.putValue(KEY_ONE, VALUE_ONE_TWO);
        map.putValue(KEY_TWO, VALUE_TWO_ONE);
        map.putValue(KEY_TWO, VALUE_TWO_TWO);

        //One test for how it currently is to ensure that everything is added.
        assertEquals("Map fullSize pre-remove is wrong", 4, map.fullSize());

        map.clear();

        assertEquals("Map size is wrong", 0, map.size());
        assertEquals("Map fullSize is wrong", 0, map.fullSize());
    }

}
