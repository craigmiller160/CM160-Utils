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

package io.craigmiller160.utils.reflect;

import io.craigmiller160.utils.sample.Custom1;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A JUnit test class for the ObjectCreator class.
 *
 * Created by craigmiller on 3/19/16.
 */
public class ObjectCreatorTest {

    @Test
    public void testInstantiateObject(){
        StringBuilder builder = ObjectCreator.instantiateClass(StringBuilder.class);

        assertNotNull("Object created is null", builder);
    }

    @Test
    public void testInstantiateWithSingleParam(){
        Custom1 custom1 = ObjectCreator.instantiateClassWithParams(Custom1.class, "Foo");

        assertNotNull("Custom1 object is null", custom1);
        assertEquals("Custom1 string field has wrong value", "Foo", custom1.getString());
    }

    @Test
    public void testInstantiateWithEmptyVarargs(){
        Custom1 custom1 = ObjectCreator.instantiateClassWithParams(Custom1.class, true);

        assertNotNull("Custom1 object is null", custom1);
        assertEquals("Custom1 boolean field has wrong value", true, custom1.getBool());
    }

    @Test
    public void testInstantiateWithVarargs(){
        Custom1 custom1 = ObjectCreator.instantiateClassWithParams(Custom1.class, true, 1, 2);

        assertNotNull("Custom1 object is null", custom1);
        assertEquals("Custom1 boolean field has wrong value", true, custom1.getBool());
        assertEquals("Custom1 one field has wrong value", 1, custom1.getOne());
        assertEquals("Custom1 two field has wrong value", 2, custom1.getTwo());
    }

}
