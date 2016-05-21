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

package io.craigmiller160.utils.sample;

/**
 * A simple value object, designed to be
 * used to test the SuperWeakHashMap as both
 * keys and values.
 *
 * Created by craig on 5/4/16.
 */
public class Val {

    private final String val;

    public Val(String val){
        this.val = val;
    }

    public String getVal(){
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Val val1 = (Val) o;

        if (val != null ? !val.equals(val1.val) : val1.val != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return val != null ? val.hashCode() : 0;
    }

    @Override
    public String toString(){
        return val;
    }

}
