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

    /**
     * A special map for comparing primitive types to wrapper types.
     * Its keys are primitive class types, and its values are wrapper
     * class types. Each key-value pair is obviously matched by being
     * the same core type, for example, int & Integer.
     */
    private static final Map<Class<?>,Class<?>> primitiveWrapperMap = new HashMap<Class<?>,Class<?>>(){{
        put(int.class, Integer.class);
        put(float.class, Float.class);
        put(double.class, Double.class);
        put(short.class, Short.class);
        put(byte.class, Byte.class);
        put(long.class, Long.class);
        put(boolean.class, Boolean.class);
        put(char.class, Character.class);
    }};

    /**
     * Because primitive values have their own assignability rules, this
     * map contains them. It stores which primitive types can be safely passed
     * to a method accepting a particular primitive.
     */
    private static final MultiValueMap<Class<?>,Class<?>> primitiveAssignableMap = new MultiValueMap<Class<?>,Class<?>>(){{
        //int
        putValue(int.class, int.class);
        putValue(int.class, short.class);
        putValue(int.class, byte.class);
        putValue(int.class, char.class);

        //float
        putValue(float.class, float.class);
        putValue(float.class, int.class);
        putValue(float.class, short.class);
        putValue(float.class, byte.class);
        putValue(float.class, long.class);
        putValue(float.class, char.class);

        //double
        putValue(double.class, double.class);
        putValue(double.class, char.class);
        putValue(double.class, int.class);
        putValue(double.class, float.class);
        putValue(double.class, short.class);
        putValue(double.class, long.class);

        //short
        putValue(short.class, short.class);
        putValue(short.class, byte.class);

        //byte
        putValue(byte.class, byte.class);

        //long
        putValue(long.class, long.class);
        putValue(long.class, int.class);
        putValue(long.class, short.class);
        putValue(long.class, byte.class);
        putValue(long.class, char.class);

        //boolean
        putValue(boolean.class, boolean.class);

        //char
        putValue(char.class, char.class);
    }};

    MethodUtils(){}

    /**
     * Convert the parameters passed to this method for a varargs invocation
     * on the Method object provided. This is done by determining which of
     * the newParams objects would match up with the varargs position in
     * the method, and then adding them to an array of the appropriate type.
     * Finally, this method creates a new array, consisting of the non-varargs
     * params plus the new varargs array, so that this new parameter array
     * can be safely used to reflectively invoke a varargs method.
     *
     * If the newParams array has no varargs values, and this is instead
     * intended to invoke the method with 0 optional varargs params, then
     * this method instead returns an empty array to fill the space the method
     * expects.
     *
     * IMPORTANT: This method does NO validation about whether or not the method
     * provided is actually a varargs method. It simply assumes that it is. To
     * do such validation, the method's isVarArgs() should be called before using
     * this method.
     *
     * It also does NO validation about whether or not this method can be invoked
     * with the provided params. To do that, use the separate isValidInvocation(...)
     * method in this class.
     *
     * @param method the varargs method to convert the params for.
     * @param newParams the params to convert for the varargs method.
     * @return an array of parameters all set to be used to reflectively invoke the varargs method.
     */
    public static Object[] convertParamsForVarArgsMethod(Method method, Object...newParams){
        if(!method.isVarArgs()){
            return newParams;
        }

        return ParamUtils.convertParamsForVarArgs(method.getParameterTypes(), newParams);
    }

    /**
     * Test if the method provided can be invoked with the provided
     * parameters.
     *
     * @param method the method to test for valid invocation.
     * @param newParams the parameters to test that they are valid for invoking the method.
     * @return true if the method can be invoked with the provided parameters.
     */
    public static boolean isValidInvocation(Method method, Object...newParams){
        return ParamUtils.isValidInvocation(method.getParameterTypes(), method.isVarArgs(), newParams);
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
