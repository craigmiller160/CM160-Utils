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
        Object result = null;
        boolean success = false;

        int actualParamCount = newParams != null ? newParams.length : 0; //TODO needs to be modified to better handle multiples
        List<ObjectAndMethod> potentialMatches = getPotentialMatchingMethods(methodSig, actualParamCount, objects);

        Map<ObjectAndMethod,Object[]> finalMatches = new HashMap<>();
        for(ObjectAndMethod oam : potentialMatches){ //TODO can probably be merged with method that accepts a Collection<ObjectAndMethod>
            Object[] finalParams = MethodUtils.validateInvocationAndConvertParams(oam.getReflectiveComponent(), newParams);
            if(finalParams != null){
//                result = RemoteInvoke.invokeMethod(oam, finalParams);
//                success = true;
//                break;
                finalMatches.put(oam, finalParams);
            }
        }

        //TODO consider switch, might be more elegant here
        if(finalMatches.size() == 0){
            throw new NoMethodException("No matching method found: " + methodSig + " " + Arrays.toString(newParams));
        }
        //If only one match, invoke the entry
        else if(finalMatches.size() == 1){
            Set<Map.Entry<ObjectAndMethod,Object[]>> entries = finalMatches.entrySet();
            for(Map.Entry<ObjectAndMethod,Object[]> entry : entries){
                result = RemoteInvoke.invokeMethod(entry.getKey(), entry.getValue());
            }
        }
        else{
            Set<Map.Entry<ObjectAndMethod,Object[]>> entries = finalMatches.entrySet();
            Map.Entry<ObjectAndMethod,Object[]> invokeEntry = null;
            for(Map.Entry<ObjectAndMethod,Object[]> entry : entries){
                boolean ambiguous = true; //TODO this needs to be set to false before the end of the next iteration
                if(invokeEntry == null){
                    invokeEntry = entry;
                    continue;
                }

                Method invokeMethod = invokeEntry.getKey().getReflectiveComponent();
                Class<?>[] invokeParamTypes = invokeMethod.getParameterTypes();
                Object[] invokeParams = invokeEntry.getValue();
                Method entryMethod = entry.getKey().getReflectiveComponent();
                Class<?>[] entryParamTypes = entryMethod.getParameterTypes();
                Object[] entryParams = entry.getValue(); //TODO need to use these params too

                if(invokeMethod.isVarArgs() && !entryMethod.isVarArgs()){
                    invokeEntry = entry;
                    ambiguous = false;
                }
                else if(!invokeMethod.isVarArgs() && entryMethod.isVarArgs()){
                    continue;
                }
                else{
                    for(int i = 0; i < invokeParamTypes.length; i++){
                        if(invokeParamTypes[i].equals(entryParamTypes[i]) || ParamUtils.isAcceptablePrimitive(invokeParamTypes[i], entryParamTypes[i])){
                            continue;
                        }
                        else if(invokeParamTypes[i].isAssignableFrom(entryParamTypes[i])){
                            if(entryParams != null && entryParams[i] != null){
                                invokeEntry = entry;
                                ambiguous = false;
                                break;
                            }
                            //TODO ensure that if the above condition isn't met, the ambiguous exception will be thrown
                        }
                        else if(entryParamTypes[i].isAssignableFrom(invokeParamTypes[i])){
                            if(invokeParams != null && invokeParams[i] != null){
                                ambiguous = false;
                                break;
                            }
                            //TODO ensure that if the above condition isn't met, the ambiguous exception will be thrown
                        }
                    }
                }

                if(ambiguous){
                    throw new NoMethodException(String.format("Ambiguous Method call: Multiple methods match signature %1$x with params %2$s",
                            methodSig, Arrays.toString(newParams)));
                }


                //TODO what about different param counts (due to possible varargs)
                //TODO when varargs and non varargs, and the actualParams don't have varargs, it goes with the non-varargs

                /*
                 * Go through each parameter
                 * Test for equality, if any two methods are equal, then throw exception
                 * Use isAssignableFrom to find which is more specific
                 * If the actual param for that index is null, throw exception
                 */

            }

            result = RemoteInvoke.invokeMethod(invokeEntry.getKey(), invokeEntry.getValue());
        }

        return result;
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
     * Find and invoke a single method that matches the provided parameters
     * and can be successfully invoked.
     *
     * @param oams the group of ObjectAndMethod objects to find a method in.
     * @param newParams the new parameters to use to invoke
     * @return the return value, if there is any.
     * @throws ReflectiveException If unable to reflectively invoke the method.
     */
    public static Object findInvokeOneMethod(Collection<ObjectAndMethod> oams, Object... newParams) throws ReflectiveException{
        Object result = null;
        boolean success = false;
        for(ObjectAndMethod oam : oams){ //TODO needs to be modified to better handle multiples
            Object[] finalParams = MethodUtils.validateInvocationAndConvertParams(oam.getReflectiveComponent(), newParams);
            if(finalParams != null){
                result = RemoteInvoke.invokeMethod(oam, finalParams);
                success = true;
                break;
            }
        }

        if(!success){
            throw new NoMethodException(String.format("No provided method can be invoked with the provided params. " +
                    "Params: %s", Arrays.toString(newParams)));
        }

        return result;
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
