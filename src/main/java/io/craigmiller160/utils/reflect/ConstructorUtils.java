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

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * Created by craig on 5/5/16.
 */
public class ConstructorUtils {

//    public static Object[] convertParamsForVarArgsConstructor(Constructor<?> constructor, Object... newParams){
//        if(!constructor.isVarArgs()){
//            return newParams;
//        }
//
//        return ParamUtils.convertParamsForVarArgs(constructor.getParameterTypes(), newParams);
//    }
//
//    public static boolean isValidInvocation(Constructor<?> constructor, Object...newParams){
//        return ParamUtils.isValidInvocation(constructor.getParameterTypes(), constructor.isVarArgs(), newParams);
//    }

    public static Object[] validateInvocationAndConvertParams(Constructor<?> constructor, Object...newParams){
        return ParamUtils.validateInvocationAndConvertParams(constructor.getParameterTypes(), constructor.isVarArgs(), newParams);
    }

    /**
     * Test if the two constructors are dupliates. They are
     * duplicates if they have the same names and parameter types.
     *
     * @param c1 the first constructor.
     * @param c2 the second constructor.
     * @return true if the two constructors are equal
     */
    public static boolean isDuplicateConstructor(Constructor<?> c1, Constructor<?> c2){
        boolean duplicate = false;
        if(c1.getName().equals(c2.getName())){
            Class<?>[] c1ParamTypes = c1.getParameterTypes();
            Class<?>[] c2ParamTypes = c2.getParameterTypes();
            if(c1ParamTypes.length == c2ParamTypes.length){
                if(Arrays.equals(c1ParamTypes, c2ParamTypes)){
                    duplicate = true;
                }
            }
        }

        return duplicate;
    }

}
