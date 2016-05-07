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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A special utility class to parse
 * parameters prior to a varArgs invocation.
 *
 * Created by craig on 5/5/16.
 */
public class ParamUtils {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(ParamUtils.class);

    /**
     * A special map for comparing primitive types to wrapper types.
     * This map uses the primitive type as the key, and returns its
     * wrapper type.
     */
    private static final Map<Class<?>,Class<?>> primitiveToWrapperMap = new HashMap<Class<?>,Class<?>>(){{
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
     * A special map for comparing primitive types to wrapper types.
     * This map uses the wrapper type as the key, and returns its
     * primitive type.
     */
    private static final Map<Class<?>,Class<?>> wrapperToPrimitiveMap = new HashMap<Class<?>,Class<?>>(){{
        put(Integer.class, int.class);
        put(Float.class, float.class);
        put(Double.class, double.class);
        put(Short.class, short.class);
        put(Byte.class, byte.class);
        put(Long.class, long.class);
        put(Boolean.class, boolean.class);
        put(Character.class, char.class);
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

    /**
     * Validate that the actual parameters can be safely passed to a method or
     * constructor with the provided expected parameter types. In addition to
     * validation, if the source method/constructor is varArgs, the parameters
     * are converted to be able to work with a varargs reflective invocation.
     *
     * This method returns an array of parameters after completing. If the method
     * is not varargs, this array is the same one that was passed to it in the final
     * argument. If the method is varargs, this array contains the parameters after
     * being converted so they can be reflectively invoked on the varargs method.
     *
     * Either way, calling classes should test the return value for null to see
     * if validation passed, and use the returned parameters for the actual
     * reflective invocation.
     *
     * @param expectedTypes the expected parameter types.
     * @param isVarArgs if the method is varargs.
     * @param actualParams the actual parameters provided.
     * @return an array of (possibly converted) parameters if the validation
     * passes, null if the validation fails.
     */
    public static Object[] validateInvocationAndConvertParams(Class<?>[] expectedTypes, boolean isVarArgs, Object...actualParams){
        int expectedTypeCount = expectedTypes.length;
        int actualParamCount = actualParams.length;

        logger.trace("Attempting to validate and convert parameters. ExpectedTypeCount: {}, ActualParamCount: {}, IsVarArgs: {}", expectedTypeCount, actualParamCount, isVarArgs);

        if(actualParamCount > 0){
            if(actualParamCount > expectedTypeCount){
                //If more params are provided than are contained in the method, the method MUST be varArgs with multiple arguments to that varargs position
                if(isVarArgs){
                    int varArgsSize = actualParamCount - expectedTypeCount + 1;
                    actualParams = validateAndConvertForVarArgs(expectedTypes, varArgsSize, actualParams);
                }
                //If not varargs, then the number of params can't be invoked on the expected types, and null is returned
                else{
                    actualParams = null;
                }
            }
            else if(actualParamCount == expectedTypeCount){
                //If their lengths are equal, may or may not be varargs. If it is, only a single argument is in the varargs position
                if(isVarArgs){
                    actualParams = validateAndConvertForVarArgs(expectedTypes, 1, actualParams);
                }
                else{
                    //If the parameters provided do not match what is expected, invocation is not possible, and null is returned
                    if(!validateParamsNoVarArgs(expectedTypes, actualParams)){
                        actualParams = null;
                    }
                }
            }
            else if(actualParamCount == expectedTypeCount - 1){
                //If provided params are one less than expected, the method MUST be varArgs with no arguments in the varargs position
                if(isVarArgs){
                    actualParams = validateAndConvertForVarArgs(expectedTypes, 0, actualParams);
                }
                //Otherwise, the number of provided parameters can't be passed to the expected types, and the method should return null
                else{
                    actualParams = null;
                }
            }
            //If none of the above conditions are met, than the required number of params was not submitted and the method is not a match
        }
        //No actual parameters have been provided to get to this point
        else{
            //If the expected number of params is 1, and we're dealing with varargs, convert the params to just have an empty varargs position
            if(expectedTypeCount == 1 && isVarArgs){
                actualParams = Arrays.copyOf(actualParams, 1);
                actualParams[0] = Array.newInstance(expectedTypes[0].getComponentType(), 0);
            }
            //If the expectedTypeCount is greater than 0 and it's not varargs, then this is invalid and should return null
            else if(expectedTypeCount > 0 && !isVarArgs){
                actualParams = null;
            }
            //Otherwise, expectedTypeCount == 0 and no params were supplied, meaning everything is good to go
        }

        return actualParams;
    }

    /**
     * Continue validating the parameter types, and perform the varArgs
     * specific conversions that need to happen. This method expects to
     * be working with varArgs, so that should be tested for before
     * calling this method.
     *
     * Like the method that calls it, this one returns null if validation
     * fails, and returns an array of converted parameters if it succeeds.
     *
     * @param expectedTypes the expected parameter types.
     * @param varArgsSize the number of actualParams that will be a part of the varArgs array.
     * @param actualParams the actual parameters provided.
     * @return the converted parameters, or null if they fails validation.
     */
    private static Object[] validateAndConvertForVarArgs(Class<?>[] expectedTypes, int varArgsSize, Object... actualParams){
        int expectedTypeCount = expectedTypes.length;
        int actualParamCount = actualParams.length;

        Object[] finalParams = new Object[expectedTypeCount];

        for(int finalIndex = 0; finalIndex < expectedTypeCount; finalIndex++){
            //If it's not the last parameter, then it's not the varargs parameter yet. Just perform simple validation of if the parameter is acceptable
            if(finalIndex < expectedTypeCount - 1){
                //If any parameter type is not assignable, the loop should end and method should return false, this is not a match
                if(!expectedTypes[finalIndex].isAssignableFrom(actualParams[finalIndex].getClass()) && !isAcceptablePrimitive(expectedTypes[finalIndex], actualParams[finalIndex].getClass())) {
                    return null;
                }
                //If it doesn't fail the above test, add the param to the finalParams array
                else{
                    finalParams[finalIndex] = actualParams[finalIndex];
                }
            }
            //If it is the final param, then it's the varArgs param and should be handled in a special way
            else{
                //If only a single varargs parameter is provided, check to see if it is an array of the right type, because then no conversion is needed
                if(varArgsSize == 1){
                    if(expectedTypes[finalIndex].equals(actualParams[finalIndex].getClass()) ||
                            expectedTypes[finalIndex].isAssignableFrom(actualParams[finalIndex].getClass())){
                        finalParams[finalIndex] = actualParams[finalIndex];
                        break;
                    }
                }
                //Otherwise, validate and convert the remaining actualParams to ensure they can fit the varargs

                //Create a varargs Array reflectively, in order to handle primitives. If the varargs size is 0, an empty array will be created
                Object varArgs = Array.newInstance(expectedTypes[finalIndex].getComponentType(), varArgsSize);
                for(int varArgsIndex = 0; varArgsIndex < actualParamCount - finalIndex; varArgsIndex++) {
                    //If any of the parameters for the varargs array don't match, return null because validation failed
                    if (!expectedTypes[finalIndex].getComponentType().isAssignableFrom(actualParams[finalIndex + varArgsIndex].getClass()) &&
                            !isAcceptablePrimitive(expectedTypes[finalIndex].getComponentType(), actualParams[finalIndex + varArgsIndex].getClass())) {
                        return null;
                    } else {
                        Array.set(varArgs, varArgsIndex, actualParams[finalIndex + varArgsIndex]);
                    }
                }


                finalParams[finalIndex] = varArgs;
            }
        }

        return finalParams;
    }

    /**
     * Validate that the provided params will all work for a reflective invocation.
     *
     * This specific method is intended for use with a method/constructor that
     * is NOT varargs.
     *
     * @param expectedTypes the expected parameter types.
     * @param actualParams the params to test that they can be used for invocation.
     * @return true if the parameters pass validation.
     */
    private static boolean validateParamsNoVarArgs(Class<?>[] expectedTypes, Object...actualParams){
        int expectedTypeCount = expectedTypes.length;

        boolean result = true;
        for(int i = 0; i < expectedTypeCount; i++){
            if(!expectedTypes[i].isAssignableFrom(actualParams[i].getClass())) {
                if(!isAcceptablePrimitive(expectedTypes[i], actualParams[i].getClass())){
                    //If any parameter type is not assignable, the loop should end and method should return false, this is not a match
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * A special method to test if the two class types passed
     * to it are matching primitive types. It has the first Class
     * argument as being from the method itself, and the second
     * is the parameter trying to be passed to it. This method returns
     * true one one of several conditions:
     *
     * 1) Both types are primitives, and both are the same primitive.
     * 2) One type is a primitive, and the other is the matching wrapper type.
     * 3) Both types are primitives, but they are assignable to each other.
     *    For example, an int being passed to a method with a long.
     *
     * @param clazz1 the param type of the method itself
     * @param clazz2 the param type of the object trying to be passed to the method.
     * @return true if they pass one of the specified conditions.
     */
    public static boolean isAcceptablePrimitive(Class<?> clazz1, Class<?> clazz2){
        boolean result = false;
        if(clazz1.isPrimitive() && clazz2.isPrimitive()){
            if(clazz1.equals(clazz2)){
                result = true;
            }
            else{
                Collection<Class<?>> assignableTypes = primitiveAssignableMap.get(clazz1);
                result = assignableTypes.contains(clazz2);
            }
        }
        else if(clazz1.isPrimitive()){
            Class<?> clazz1AsWrapper = primitiveToWrapperMap.get(clazz1);
            Class<?> clazz2AsPrimitive = wrapperToPrimitiveMap.get(clazz2);

            result = clazz1.isAssignableFrom(clazz2) || clazz1AsWrapper.isAssignableFrom(clazz2) ||
                    primitiveAssignableMap.get(clazz1).contains(clazz2AsPrimitive);
        }
        else if(clazz2.isPrimitive()){
            Class<?> clazz2AsWrapper = primitiveToWrapperMap.get(clazz2);
            Class<?> clazz1AsPrimitive = wrapperToPrimitiveMap.get(clazz1);
            result = clazz1.isAssignableFrom(clazz2) || clazz1.isAssignableFrom(clazz2AsWrapper) ||
                    primitiveAssignableMap.get(clazz1AsPrimitive).contains(clazz2);
        }

        return result;
    }

}
