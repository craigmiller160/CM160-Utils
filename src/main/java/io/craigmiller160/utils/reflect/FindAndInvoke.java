/*
 * Copyright {yyyy} Craig Miller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.craigmiller160.utils.reflect;

import java.lang.reflect.InvocationTargetException;
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
     * @throws InvocationException If a normal exception occurs while trying to reflectively
     *                              invoke the method.
     * @throws NoMethodException if the method being searched for doesn't exist.
     *
     */
    public static Object findAndInvokeMethod(Object object, String methodSig, Object...newParams){
        ObjectAndMethod oam = getMatchingMethodForSingle(object, methodSig, newParams);
        if(oam == null){
            throw new NoMethodException("No matching method found: " + methodSig + " " + Arrays.toString(newParams));
        }

        return RemoteInvoke.invokeMethod(oam, newParams);
    }

    /**
     * Find and invoke the method on a single matching object, identified from the
     * array of Objects passed to this method.
     *
     * @param objects the objects to find and invoke the method on.
     * @param methodSig the signature of the method, minus params.
     * @param newParams the parameters to pass to the method.
     * @return the result of the invocation.
     * @throws ReflectiveException If unable to reflectively invoke the method.
     * @throws InvocationException If a normal exception occurs while trying to reflectively
     *                              invoke the method.
     * @throws NoMethodException if the method being searched for doesn't exist.
     */
    public static Object findAndInvokeMethod(Object[] objects, String methodSig, Object...newParams){
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
     * @throws InvocationException If a normal exception occurs while trying to reflectively
     *                              invoke the method.
     * @throws NoMethodException if the method being searched for doesn't exist.
     */
    public static Object findAndInvokeMethod(Collection<?> objects, String methodSig, Object...newParams)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return findAndInvokeMethod(objects.toArray(), methodSig, newParams);
    }


    private static ObjectAndMethod getMatchingMethod(Object[] objects, String methodSig, Object...newParams) {
        List<ObjectAndMethod> potentialMatches = getPotentialMatchingMethodsFromMultiple(objects, methodSig);

        for(ObjectAndMethod oam : potentialMatches){
            if(MethodUtils.isValidInvocation(oam.getMethod(), newParams)){
                return oam;
            }
        }
        return null;
    }

    private static ObjectAndMethod getMatchingMethodForSingle(Object object, String methodSig, Object...newParams) {
        List<ObjectAndMethod> potentialMatches = getPotentialMatchesFromSingle(methodSig, object);

        for(ObjectAndMethod oam : potentialMatches){
            if(MethodUtils.isValidInvocation(oam.getMethod(), newParams)){
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
            matchingMethods.addAll(getPotentialMatchesFromSingle(methodSig, obj));
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
    private static List<ObjectAndMethod> getPotentialMatchesFromSingle(String methodSig, Object obj){
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
