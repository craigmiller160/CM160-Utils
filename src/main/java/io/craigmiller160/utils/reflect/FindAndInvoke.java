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

package io.craigmiller160.utils.reflect;

import io.craigmiller160.utils.util.ArrayUtil;
import io.craigmiller160.utils.util.Pair;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parse the provided Object or Objects and find the specified method,
 * and then remotely invokes it.
 *
 * Created by Craig on 2/14/2016.
 */
public class FindAndInvoke {

    //TODO consider eliminating the overloading of the methods

    //TODO remove these comments
    /*
     * How it might work:
     *
     * If there's just one result after param validation, invoke it
     * If more than one, there need to be rules
     *
     * 1) Null arguments: If multiple methods have non-primitive arguments that are being passed null, an exception should be thrown for ambiguity
     * 2) If no arguments are null, the methods should be compared for inheritance. The more specific implementation should win out.
     *
     * Iterate through the results once. Assign the first value to a final ObjectAndMethod
     * Each subsequent iteration, compare all parameters. Check if the arg for that position is null, if it is and there are multiple object types, throw exception
     */

    //TODO test ideas for this:
    // 1) Overloaded method: One with object, one with String. Passing an object should get the Object, passing a String should get the String
    // 2) Overloaded method with null - ambiguous.
    // 3) Actual duplicate methods - should cause exception for the invokeOne


    /**
     * Find and invoke the method on a single matching object, identified from the
     * array of Objects passed to this method.
     *
     * @param objects the objects to find and invoke the method on.
     * @param methodSig the signature of the method, minus params.
     * @param newParams the parameters to pass to the method.
     * @return the result of the invocation.
     * @throws ReflectiveException If unable to reflectively invoke the method.
     */
    public static Object findInvokeOneMethod(String methodSig, Object[] objects, Object... newParams) throws ReflectiveException{
        int actualParamCount = newParams != null ? newParams.length : 0;
        List<ObjectAndMethod> potentialMatches = getPotentialMatchingMethods(methodSig, actualParamCount, objects);

        return performInvocation(methodSig, potentialMatches, true, newParams);
    }

    /**
     * Find and invoke the method on a single matching object, identified from the
     * collection of Objects passed to this method.
     *
     * @param objects the objects to find and invoke the method on.
     * @param methodSig the signature of the method, minus params.
     * @param newParams the parameters to pass to the method.
     * @return the result of the invocation.
     * @throws ReflectiveException If unable to reflectively invoke the method.
     */
    public static Object findInvokeOneMethod(String methodSig, Collection<?> objects, Object... newParams)
            throws ReflectiveException {
        return findInvokeOneMethod(methodSig, objects.toArray(), newParams);
    }

    /**
     * Parse all potentially matching methods, and either perform the
     * invocation or throw an exception. It returns the result of the
     * invocation ONLY if the boolean single parameter is true. For
     * multiple invocations, it will always return null.
     *
     * @param methodSig the signature of the method.
     * @param oams the potentially matching methods.
     * @param single if this is a single invocation.
     * @param newParams the parameters to use for the invocation.
     * @return the result of the invocation. Will always be null for multiple invocations.
     * @throws ReflectiveException if unable to reflectively invoke any methods.
     */
    private static Object performInvocation(String methodSig, Collection<ObjectAndMethod> oams,
                                            boolean single, Object...newParams) throws ReflectiveException{
        long start = System.currentTimeMillis(); //TODO delete this
        Object result = null;
        boolean success = false;

        Pair<ObjectAndMethod,Object[]> singleInvoke = null;
        for(ObjectAndMethod oam : oams){
            Object[] finalParams = MethodUtils.validateInvocationAndConvertParams(oam.getReflectiveComponent(), newParams);
            //If the params weren't valid, don't perform any of the additional validation
            if(finalParams == null){
                continue;
            }

            //Parse multiple options for invocation if single is true, or just invoke if single is false
            if(single){
                if(singleInvoke == null){
                    singleInvoke = new Pair<>(oam, finalParams);
                }
                else{
                    singleInvoke = chooseInvocation(methodSig, singleInvoke.getFirst(), singleInvoke.getSecond(), oam, finalParams);
                }
            }
            else{
                RemoteInvoke.invokeMethod(oam, finalParams);
                success = true;
            }
        }

        if(single && singleInvoke != null){
            result = RemoteInvoke.invokeMethod(singleInvoke.getFirst(), singleInvoke.getSecond());
            success = true;
        }

        if(!success){
            throw new NoMethodException(String.format("No provided method can be invoked with the provided params. " +
                    "Params: %s", Arrays.toString(newParams)));
        }

        long end = System.currentTimeMillis(); //TODO delete this
        System.out.println("MAIN: " + (end - start)); //TODO delete this

        return result;
    }

    /**
     * Compare two potential invocations to find the one that should actually
     * be invoked.
     *
     * @param methodSig the signature of the method.
     * @param invokeOam the currently selected method to invoke.
     * @param invokeParams the currently selected params to invoke.
     * @param otherOam the other possible method to invoke.
     * @param otherParams the other possible params to invoke.
     * @return the Pair of method and params to invoke.
     * @throws ReflectiveException if the comparison is ambiguous and an invocation cannot be made.
     */
    private static Pair<ObjectAndMethod,Object[]> chooseInvocation(String methodSig, ObjectAndMethod invokeOam, Object[] invokeParams,
                                                                   ObjectAndMethod otherOam, Object[] otherParams) throws ReflectiveException{
        Class<?>[] invokeParamTypes = invokeOam.getReflectiveComponent().getParameterTypes();
        Class<?>[] otherParamTypes = otherOam.getReflectiveComponent().getParameterTypes();

        //If only one is varargs, go with the non-varargs one, because if they both matched then the varargs params just include an empty array
        if(invokeOam.isVarArgs() && !otherOam.isVarArgs()){
            return new Pair<>(otherOam, otherParams);
        }
        else if(!invokeOam.isVarArgs() && otherOam.isVarArgs()){
            return new Pair<>(invokeOam, invokeParams);
        }
        //Otherwise, parse the param types. default to the more specific type. if a null argument is provided to any position, an exception will ultimately be thrown
        else{
            for(int i = 0; i < invokeParamTypes.length; i++){
                if(invokeParamTypes[i].equals(otherParamTypes[i]) || ParamUtils.isAcceptablePrimitive(invokeParamTypes[i], otherParamTypes[i])){
                    continue;
                }
                else if(invokeParamTypes[i].isAssignableFrom(otherParamTypes[i])){
                    if(otherParams != null && otherParams[i] != null){
                        return new Pair<>(otherOam, otherParams);
                    }
                    //if the above condition isn't met, the ambiguous exception will be thrown
                }
                else if(otherParamTypes[i].isAssignableFrom(invokeParamTypes[i])){
                    if(invokeParams != null && invokeParams[i] != null){
                        return new Pair<>(invokeOam, invokeParams);
                    }
                    //if the above condition isn't met, the ambiguous exception will be thrown
                }
            }

            //If it gets here, then it's an ambiguous call and an exception should be thrown
            if(methodSig != null){
                throw new NoMethodException(String.format("Ambiguous Method call: Multiple methods match signature %1$s with params %2$s",
                        methodSig, ArrayUtil.deepToString(invokeParams)));
            }
            else{
                throw new NoMethodException(String.format("Ambiguous Method call: Multiple methods could be called with params %1$s",
                        ArrayUtil.deepToString(invokeParams)));
            }
        }
    }

    //TODO ultimately, merge this with the invokeAll stuff as well
    /**
     * Parse all potentially matching methods, and either perform the
     * invocation or throw an exception.
     *
     * @param methodSig the method signature.
     * @param oams the potentially matching methods.
     * @param newParams the parameters to use for invocation.
     * @return the result of the invocation.
     * @throws ReflectiveException if unable to reflectively invoke the method.
     */



    /**
     * Find and invoke a single method that matches the provided parameters
     * and can be successfully invoked.
     *
     * @param oams the group of ObjectAndMethod objects to find a method in.
     * @param newParams the new parameters to use to invoke
     * @return the return value, if there is any.
     * @throws ReflectiveException If unable to reflectively invoke the method.
     */
    public static Object findInvokeOneMethod(Collection<ObjectAndMethod> oams, Object... newParams) throws ReflectiveException{
        return performInvocation(null, oams, true, newParams);
    }

    /**
     * Find any matching methods in the provided array of objects and invoke all matches.
     * No value will be returned, because potentially invoking more than one
     * method there is no way to know which one to return a value from.
     *
     * @param objects the array of objects to find and invoke methods on.
     * @param methodSig the signature of the method to find and invoke.
     * @param newParams the parameters to use for the method invocation.
     * @throws ReflectiveException if unable to find or invoke the method.
     */
    public static void findInvokeAllMethods(String methodSig, Object[] objects, Object... newParams) throws ReflectiveException{
        int actualParamCount = newParams != null ? newParams.length : 0;
        List<ObjectAndMethod> potentialMatches = getPotentialMatchingMethods(methodSig, actualParamCount, objects);

        if(potentialMatches == null || potentialMatches.size() == 0){ //TODO needs to be modified to better handle multiples
            throw new NoMethodException(String.format("No method in provided objects match signature %1$s.", methodSig));
        }

        attemptToInvokeAllMethods(potentialMatches, newParams);
    }

    /**
     * Find any matching methods in the provided collection of objects and invoke all matches.
     * No value will be returned, because potentially invoking more than one
     * method there is no way to know which one to return a value from.
     *
     * @param objects the array of objects to find and invoke methods on.
     * @param methodSig the signature of the method to find and invoke.
     * @param newParams the parameters to use for the method invocation.
     * @throws ReflectiveException if unable to find or invoke the method.
     */
    public static void findInvokeAllMethods(String methodSig, Collection<Object> objects, Object... newParams) throws ReflectiveException{
        findInvokeAllMethods(methodSig, objects.toArray(), newParams);
    }

    /**
     * Find any matching methods in the provided collection of ObjectAndMethods and invoke all matches.
     * No value will be returned, because potentially invoking more than one
     * method there is no way to know which one to return a value from.
     *
     * @param oams the array of objects and methods to find and invoke methods on.
     * @param newParams the parameters to use for the method invocation.
     * @throws ReflectiveException if unable to find or invoke the method.
     */
    public static void findInvokeAllMethods(Collection<ObjectAndMethod> oams, Object... newParams) throws ReflectiveException{
        attemptToInvokeAllMethods(oams, newParams);
    }

    /**
     * Attempt to invoke all provided methods on their matching objects, using
     * the parameters provided. If no matching methods are found, an
     * exception will be thrown.
     *
     * No values are returned from the invocations, due to the fact
     * that invoking multiple methods doesn't make clear which
     * value should be returned.
     *
     * @param oams the objects and methods to attempt to invoke.
     * @param newParams the parameters to use for the invocation.
     * @throws NoMethodException if no matching methods are found.
     */
    private static void attemptToInvokeAllMethods(Collection<ObjectAndMethod> oams, Object... newParams) throws NoMethodException{
        boolean success = false;
        for(ObjectAndMethod oam : oams){ //TODO needs to be modified to better handle multiples
            Object[] finalParams = MethodUtils.validateInvocationAndConvertParams(oam.getReflectiveComponent(), newParams);
            if(finalParams != null){
                RemoteInvoke.invokeMethod(oam, newParams);
                success = true;
            }
        }

        if(!success){
            throw new NoMethodException(String.format("No provided method can be invoked with the provided params. " +
                    "Params: %s", Arrays.toString(newParams)));
        }
    }

    /**
     * Get all potentially matching methods from
     * objects provided to this method. A potential match is a method whose
     * signature matches the provided String, but whose parameter types
     * haven't been checked yet.
     *
     * @param objects the collection of objects to search for a matching method.
     * @param actualParamCount the count of parameters provided for the invocation.
     * @param methodSig the signature of the method to search for a match
     *                  of.
     * @return a list of all matching methods paired with the objects they're
     *          from.
     * @throws NoMethodException if no potentially matching methods are found.
     */
    private static List<ObjectAndMethod> getPotentialMatchingMethods(String methodSig, int actualParamCount, Object...objects) {
        List<ObjectAndMethod> matchingMethods = new ArrayList<>();
        for(Object obj : objects){
            matchingMethods.addAll(getPotentialMatchingMethodsFromSingle(methodSig, obj, actualParamCount));
        }

        //If no matches are found, throw an exception
        if(matchingMethods.size() == 0){
            throw new NoMethodException("No methods exist matching this signature: " + methodSig);
        }

        return matchingMethods;
    }

    /**
     * Get all potentially matching methods from a single object.
     * A potential match is a method whose signature matches the
     * provided String, but whose parameter types haven't been
     * checked yet.
     *
     * @param methodSig the signature of the method to find a match
     *                  for.
     * @param obj the object to search for methods with a matching
     *            signature.
     * @param actualParamCount the count of parameters provided for the invocation.
     * @return a list of any potential matches found, or an empty
     *          list if none are found.
     */
    private static List<ObjectAndMethod> getPotentialMatchingMethodsFromSingle(String methodSig, Object obj, int actualParamCount){
        List<ObjectAndMethod> matches = new ArrayList<>();
        Method[] methods = obj.getClass().getMethods();
        for(Method m : methods){
            if(m.getName().equals(methodSig)){
                if(m.isVarArgs() && m.getParameterTypes().length >= actualParamCount - 1){
                    matches.add(new ObjectAndMethod(obj, m));
                }
                else if(m.getParameterTypes().length == actualParamCount){
                    matches.add(new ObjectAndMethod(obj, m));
                }
            }
        }

        return matches;
    }

}
