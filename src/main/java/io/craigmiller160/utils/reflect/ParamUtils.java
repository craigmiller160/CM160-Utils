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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by craig on 5/5/16.
 */
public class ParamUtils {

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

    //TODO document this
    public static Object[] convertParamsForVarArgs(Class<?>[] expectedTypes, Object...actualParams){
        int expectedTypesCount = expectedTypes.length;

        //If there are parameters provided, convert them
        if(actualParams.length > 0){
            //If there are more params than there are extra ones to be converted into a varargs array
            if(actualParams.length > expectedTypesCount){
                int varArgsLength = actualParams.length - expectedTypesCount + 1;
                actualParams = performVarArgsParamConversion(expectedTypes, varArgsLength, actualParams); //TODO what if there is an array and individual params???
            }
            //If there is exactly the right amount of params, ensure that the last one is either an array or put it into one
            else if(actualParams.length == expectedTypesCount){
                int varArgsIndex = expectedTypesCount - 1;
                if(expectedTypes[varArgsIndex].equals(actualParams[varArgsIndex].getClass()) ||
                        expectedTypes[varArgsIndex].isAssignableFrom(actualParams[varArgsIndex].getClass())){
                    //This is if an array is already provided
                    return actualParams;
                }
                actualParams = performVarArgsParamConversion(expectedTypes, 1, actualParams);
            }
            //If the param count is one less then expected, then convert the params with an empty varargs array provided
            else if(actualParams.length == expectedTypesCount - 1){
                actualParams = performVarArgsParamConversion(expectedTypes, 0, actualParams);
            }
            //It should never need to check anything else, because this method doesn't validate invalid invocations
            //Anything else would be an invalid invocation.
        }
        else{
            //If no new params are provided, an empty array needs to be returned.
            return (Object[]) Array.newInstance(expectedTypes[0], 0);
        }
        return actualParams;
    }

    /**
     * Convert the parameters provided for a varargs invocation. It takes
     * the parameters from the varargs index to the end of the newParams array
     * and puts them into a separate varargs array, with the size specified
     * as an argument. If the size specified is 0, an empty varargs array
     * will be created.
     *
     * After the conversion is done, everything is put into a new array
     * and returned.
     *
     * @param expectedTypes the expected parameter types, including the varArgs type.
     * @param varArgsSize the number of arguments that are being passed
     *                    to the varargs part of the method.
     * @param actualParams the parameters to convert for varargs.
     * @return the converted parameters.
     */
    private static Object[] performVarArgsParamConversion(Class<?>[] expectedTypes, int varArgsSize, Object...actualParams){
        Object[] resultArr = new Object[expectedTypes.length];
        //Add every argument up until the index of the varargs to the resultArr
        int varArgsIndex = expectedTypes.length - 1;
        for(int i = 0; i < expectedTypes.length - 1; i++){
            resultArr[i] = actualParams[i];
        }
        //Create a varargs array of the specified size. If 0, an empty array will be created
        Object varArgs = Array.newInstance(expectedTypes[varArgsIndex].getComponentType(), varArgsSize);

        //The components are assigned reflectively to accommodate for primitive arrays
        for(int i = 0; i < varArgsSize; i++){
            if(!expectedTypes[varArgsIndex].getComponentType().isAssignableFrom(actualParams[varArgsIndex + i].getClass()) &&
                    !isAcceptablePrimitive(expectedTypes[varArgsIndex].getComponentType(), actualParams[varArgsIndex + i].getClass())){
                throw new ReflectiveException(String.format("An object with type %1$s cannot be assigned to an array of component type %2$s",
                        actualParams[varArgsIndex].getClass().getName(), expectedTypes[varArgsIndex].getComponentType()));
            }
            Array.set(varArgs, i, actualParams[varArgsIndex + i]);
        }

        //Assign the varArgs array to the varArgsIndex in the resultArr, aka the last position in the array
        resultArr[varArgsIndex] = varArgs;

        return resultArr;
    }

    //TODO document this
    public static boolean isValidInvocation(Class<?>[] expectedTypes, boolean isVarArgs, Object...actualParams){
        int expectedTypeCount = expectedTypes.length;

        boolean result = false;
        if(actualParams.length > 0){
            if(actualParams.length > expectedTypeCount){
                //If more params are provided than are contained in the method, the method MUST be varArgs.
                if(isVarArgs){
                    result = validateParamsWithVarArgs(expectedTypes, actualParams);
                }
            }
            else if(actualParams.length == expectedTypeCount){
                //If their lengths are equal, may or may not be varargs.
                if(isVarArgs){
                    result = validateParamsWithVarArgs(expectedTypes, actualParams);
                }
                else{
                    result = validateParamsNoVarArgs(expectedTypes, actualParams);
                }
            }
            else if(actualParams.length == expectedTypeCount - 1){
                //If provided params are one less than expected, the method MUST be varArgs
                if(isVarArgs){
                    result = validateParamsWithEmptyVarArgs(expectedTypes, actualParams);
                }
            }
            //If none of the above conditions are met, than the required number of params was not submitted and the method is not a match
        }
        else{
            //If no newParams are provided, the method must either have no params, or 1 param that is a varArg.
            result = expectedTypeCount == 0 || (expectedTypeCount == 1 && isVarArgs);
        }

        return result;
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
     * Validate that the provided params will all work as a reflective invocation.
     *
     * This particular method is intended for use with a method/constructor that is
     * varargs and will have parameters passed to that varargs position.
     *
     * @param expectedTypes the expected parameter types.
     * @param actualParams the parameters to test that they can be used for a
     *                  varargs invocation.
     * @return true if the params pass validation.
     */
    private static boolean validateParamsWithVarArgs(Class<?>[] expectedTypes, Object...actualParams){
        int expectedTypeCount = expectedTypes.length;

        boolean result = true;
        for(int i = 0; i < expectedTypeCount; i++){
            if(i < expectedTypeCount - 1){
                //If it's not the last parameter, then it's not the varargs parameter yet
                if(!expectedTypes[i].isAssignableFrom(actualParams[i].getClass())) {
                    if(!isAcceptablePrimitive(expectedTypes[i], actualParams[i].getClass())){
                        //If any parameter type is not assignable, the loop should end and method should return false, this is not a match
                        result = false;
                        break;
                    }
                }
            }
            else{
                if(!isValidVarArgs(expectedTypes[i], Arrays.copyOfRange(actualParams, i, actualParams.length))){
                    //If the final param, and the newParams provided for that position, won't work as valid varArgs, this is not a mach
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Validate that the provided params will all work as a reflective invocation.
     *
     * This specific method is intended to be used for reflectively invoking
     * a varargs method/constructor with an empty varargs parameter.
     *
     * @param expectedTypes the expected parameter types.
     * @param actualParams the params to test that they can be used for an empty
     *                  varargs invocation.
     * @return true if the parameters pass validation.
     */
    private static boolean validateParamsWithEmptyVarArgs(Class<?>[] expectedTypes, Object...actualParams){
        int expectedTypeCount = expectedTypes.length;

        boolean result = true;
        //Only loop through all but the last argument to validate
        for(int i = 0; i < expectedTypeCount - 1; i++){
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
     * Validate that the provides parameters are all valid for use with
     * the varArg type provided.
     *
     * IMPORTANT: This method is NOT intended for use with an empty-varargs
     * invocation.
     *
     * @param varArgType the type of vararg.
     * @param varArgParams the parameters to test that they can be assigned to the vararg type.
     * @return true if the parameters are valid for the vararg type.
     */
    private static boolean isValidVarArgs(Class<?> varArgType, Object...varArgParams){
        boolean result = true;
        //If there is a single varArgParam, and it's an array already, simply compare their types and return
        if(varArgParams.length == 1 && varArgParams[0].getClass().isArray()){
            return varArgType.equals(varArgParams[0].getClass()) || varArgType.isAssignableFrom(varArgParams[0].getClass());
        }

        //Get the type of component the varArg array expects
        Class<?> arrayComponentType = varArgType.getComponentType();
        if(arrayComponentType != null){
            for(Object o : varArgParams){
                if(!arrayComponentType.isAssignableFrom(o.getClass())){
                    if(!isAcceptablePrimitive(arrayComponentType, o.getClass())){
                        //If the type of array component cannot accept the varArgParam type, end the loop and this is not a match
                        result = false;
                        break;
                    }
                }
            }
        }
        else{
            //If the arrayComponentType is null, then this method was improperly called. Meaning something is broken
            throw new RuntimeException("isValidVarArgs(...) called on a non-varArg type, check the invoking code for errors");
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
            Class<?> matchingWrapper = primitiveWrapperMap.get(clazz1);
            result = matchingWrapper.equals(clazz2);
        }
        else if(clazz2.isPrimitive()){
            Class<?> matchingWrapper = primitiveWrapperMap.get(clazz2);
            result = matchingWrapper.equals(clazz1);
        }

        return result;
    }

}