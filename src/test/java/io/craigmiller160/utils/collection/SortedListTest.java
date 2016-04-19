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

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test class for the SortedList class.
 *
 * Created by Craig on 2/10/2016.
 */
public class SortedListTest {

    //Produces the opposite of the normal sort order.
    private Comparator<String> comp1 = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return (o1.compareTo(o2) * -1);
        }
    };

    private Comparator<String> comp2 = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    };

    /**
     * Test the add method with a custom comparator.
     */
    @Test
    public void testAddComparator(){
        SortedList<String> list = new SortedList<>(comp1);
        list.add("A");
        list.add("E");
        list.add("H");
        list.add("C");
        list.add("B");
        list.add("D");
        list.add("G");
        list.add("F");

        //Expected Order: H, G, F, E, D, C, A

        assertEquals(list.get(0), "H");
        assertEquals(list.get(1), "G");
        assertEquals(list.get(2), "F");
        assertEquals(list.get(3), "E");
        assertEquals(list.get(4), "D");
        assertEquals(list.get(5), "C");
        assertEquals(list.get(6), "B");
        assertEquals(list.get(7), "A");
    }

    /**
     * Test the addAll method with a custom comparator.
     */
    @Test
    public void testAddAllComparator(){
        SortedList<String> list = new SortedList<>(comp1);
        list.add("A");
        list.add("E");
        list.add("H");
        list.add("C");


        List<String> list2 = new ArrayList<>();

        list2.add("B");
        list2.add("D");
        list2.add("G");
        list2.add("F");

        list.addAll(list2);

        //Expected Order: H, G, F, E, D, C, B, A

        assertEquals(list.get(0), "H");
        assertEquals(list.get(1), "G");
        assertEquals(list.get(2), "F");
        assertEquals(list.get(3), "E");
        assertEquals(list.get(4), "D");
        assertEquals(list.get(5), "C");
        assertEquals(list.get(6), "B");
        assertEquals(list.get(7), "A");
    }

    /**
     * Test the set method with a custom comparator.
     */
    @Test
    public void testSetComparator(){
        SortedList<String> list = new SortedList<>(comp1);
        list.add("A");
        list.add("E");
        list.add("H");
        list.add("C");

        list.set(0, "Q");

        //Expected Order: Q, E, C, A

        assertEquals(list.get(0), "Q");
        assertEquals(list.get(1), "E");
        assertEquals(list.get(2), "C");
        assertEquals(list.get(3), "A");
    }

    /**
     * Test the ListIterator's add method,
     * to ensure that it respects the ordering
     * of the sorted list.
     */
    @Test
    public void testListIteratorAdd(){
        SortedList<String> list = new SortedList<>();
        list.add("A");
        list.add("E");
        list.add("H");
        list.add("C");

        //Expected Order: A, C, E, G, H

        ListIterator<String> lit = list.listIterator();

        lit.next();
        lit.next();
        lit.add("G");

        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
        assertEquals("E", list.get(2));
        assertEquals("G", list.get(3));
        assertEquals("H", list.get(4));
    }

    /**
     * Test the set method on the ListIterator.
     */
    @Test
    public void testListIteratorSet(){
        SortedList<String> list = new SortedList<>();
        list.add("A");
        list.add("E");
        list.add("H");
        list.add("C");

        //Expected Order: A, E, G, H

        ListIterator<String> lit = list.listIterator();

        lit.next();
        lit.next();
        lit.set("G");

        assertEquals("A", list.get(0));
        assertEquals("E", list.get(1));
        assertEquals("G", list.get(2));
        assertEquals("H", list.get(3));
    }

}
