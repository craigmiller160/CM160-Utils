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

import io.craigmiller160.utils.collection.MultiValueMap;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for operations involving methods that
 * need to be performed before they can be reflectively
 * invoked.
 *
 * Created by Craig on 2/14/2016.
 */
public class MethodUtils {

    MethodUtils(){}

    public static Object[] validateInvocationAndConvertParams(Method method, Object...newParams){
        return ParamUtils.validateInvocationAndConvertParams(method.getParameterTypes(), method.isVarArgs(), newParams);
    }

     /**
     * Compare the two provided methods and test if they are duplicates.
     * This is different from Method.equals(...), because this method
     * only takes into account method name and argument types, not
     * the owning class. This is designed to weed out methods from
     * separate classes that are otherwise identical.
     *
     * @param m1 the first method to test.
     * @param m2 the second method to test.
     * @return true if the two methods are duplicates.
     */
    public static boolean isDuplicateMethod(Method m1, Method m2){
        boolean duplicate = false;
        if(m1.getName().equals(m2.getName())){
            Class<?>[] m1ParamTypes = m1.getParameterTypes();
            Class<?>[] m2ParamTypes = m2.getParameterTypes();
            if(m1ParamTypes.length == m2ParamTypes.length){
                if(Arrays.equals(m1ParamTypes, m2ParamTypes)){
                    duplicate = true;
                }
            }
        }

        return duplicate;
    }
}
