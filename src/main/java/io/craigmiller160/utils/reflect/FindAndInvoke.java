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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Parse the provided Object or Objects and find the specified method,
 * and then remotely invokes it.
 *
 * Created by Craig on 2/14/2016.
 */
public class FindAndInvoke {

    /**
     * Find and invoke the method from a single object.
     *
     * @param object the object to find and invoke the method on.
     * @param methodSig the signature of the method, minus params.
     * @param newParams the parameters to pass to the method.
     * @return the result of the invocation.
     * @throws ReflectiveException If unable to reflectively invoke the method.
     *
     */
    //TODO removing the single object methods because of ambiguous method calls. In the future, maybe find a way to restore this
//    public static Object findInvokeOneMethod(Object object, String methodSig, Object... newParams) throws ReflectiveException{
//        ObjectAndMethod oam = getMatchingMethodForSingle(object, methodSig, newParams);
//        if(oam == null){
//            throw new NoMethodException("No matching method found: " + methodSig + " " + Arrays.toString(newParams));
//        }
//
//        return RemoteInvoke.invokeMethod(oam, newParams);
//    }

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
        ObjectAndMethod oam = getMatchingMethod(objects, methodSig, newParams);
        if(oam == null){
            throw new NoMethodException("No matching method found: " + methodSig + " " + Arrays.toString(newParams));
        }

        return RemoteInvoke.invokeMethod(oam, newParams);
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
        for(ObjectAndMethod oam : oams){
            if(MethodUtils.isValidInvocation(oam.getReflectiveComponent(), newParams)){
                result = RemoteInvoke.invokeMethod(oam, newParams);
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
     * Find any matching methods in the provided object and invoke all matches.
     * No value will be returned, because potentially invoking more than one
     * method there is no way to know which one to return a value from.
     *
     * @param object the object to find and invoke methods on.
     * @param methodSig the signature of the method to find and invoke.
     * @param newParams the parameters to use for the method invocation.
     * @throws ReflectiveException if unable to find or invoke the method.
     */
    //TODO removing the single object methods because of ambiguous method calls. In the future, maybe find a way to restore this
//    public static void findInvokeAllMethods(Object object, String methodSig, Object... newParams) throws ReflectiveException{
//        List<ObjectAndMethod> potentialMatches = getPotentialMatchingMethodsFromSingle(methodSig, object);
//
//        if(potentialMatches == null || potentialMatches.size() == 0){
//            throw new NoMethodException(String.format("No method in object %1$s matches signature %2$s", object.getClass().getName(), methodSig));
//        }
//
//        attemptToInvokeAllMethods(potentialMatches, newParams);
//    }

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
        List<ObjectAndMethod> potentialMatches = getPotentialMatchingMethodsFromMultiple(objects, methodSig);

        if(potentialMatches == null || potentialMatches.size() == 0){
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
        for(ObjectAndMethod oam : oams){
            if(MethodUtils.isValidInvocation(oam.getReflectiveComponent(), newParams)){
                RemoteInvoke.invokeMethod(oam, newParams);
                success = true;
            }
        }

        if(!success){
            throw new NoMethodException(String.format("No provided method can be invoked with the provided params. " +
                    "Params: %s", Arrays.toString(newParams)));
        }
    }


    private static ObjectAndMethod getMatchingMethod(Object[] objects, String methodSig, Object...newParams) {
        List<ObjectAndMethod> potentialMatches = getPotentialMatchingMethodsFromMultiple(objects, methodSig);

        for(ObjectAndMethod oam : potentialMatches){
            if(MethodUtils.isValidInvocation(oam.getReflectiveComponent(), newParams)){
                return oam;
            }
        }
        return null;
    }

    private static ObjectAndMethod getMatchingMethodForSingle(Object object, String methodSig, Object...newParams) {
        List<ObjectAndMethod> potentialMatches = getPotentialMatchingMethodsFromSingle(methodSig, object);

        for(ObjectAndMethod oam : potentialMatches){
            if(MethodUtils.isValidInvocation(oam.getReflectiveComponent(), newParams)){
                return oam;
            }
        }
        return null;
    }

    /**
     * Get all potentially matching methods from the collection of
     * objects provided to this class. A potential match is a method whose
     * signature matches the provided String, but whose parameter types
     * haven't been checked yet.
     *
     * @param objects the collection of objects to search for a matching method.
     * @param methodSig the signature of the method to search for a match
     *                  of.
     * @return a list of all matching methods paired with the objects they're
     *          from.
     * @throws NoMethodException if no potentially matching methods are found.
     */
    private static List<ObjectAndMethod> getPotentialMatchingMethodsFromMultiple(Object[] objects, String methodSig) {
        List<ObjectAndMethod> matchingMethods = new ArrayList<>();
        for(Object obj : objects){
            matchingMethods.addAll(getPotentialMatchingMethodsFromSingle(methodSig, obj));
        }

        //If no matches are found, throw an exception
        if(matchingMethods.size() == 0){
            throw new NoMethodException("No methods exist matching this signature: " + methodSig);
        }

        return matchingMethods;
    }

    /**
     * Get all potentially matching methods from a single class.
     * A potential match is a method whose signature matches the
     * provided String, but whose parameter types haven't been
     * checked yet.
     *
     * @param methodSig the signature of the method to find a match
     *                  for.
     * @param obj the object to search for methods with a matching
     *            signature.
     * @return a list of any potential matches found, or an empty
     *          list if none are found.
     */
    private static List<ObjectAndMethod> getPotentialMatchingMethodsFromSingle(String methodSig, Object obj){
        List<ObjectAndMethod> matches = new ArrayList<>();
        Method[] methods = obj.getClass().getMethods();
        for(Method m : methods){
            if(m.getName().equals(methodSig)){
                matches.add(new ObjectAndMethod(obj, m));
            }
        }

        return matches;
    }

}
