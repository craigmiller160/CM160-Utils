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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * A simple utility class to instantiate
 * objects, wrapping any exceptions that occur
 * in a ReflectiveException.
 *
 * Created by craigmiller on 3/19/16.
 */
public class ObjectCreator {

    private static final Logger logger = LoggerFactory.getLogger(ObjectCreator.class);

    public static <T>  T instantiateClass(Class<T> type) throws ReflectiveException{
        return instantiateClassWithParams(type);
    }

    public static <T> T instantiateClassWithParams(Class<T> type, Object...params) throws ReflectiveException{
        T result = null;

        try{
            if(params.length == 0){
                result = type.newInstance();
                logger.trace("Successfully instantiated new instance of class {}", type.getName());
            }
            else{
                Constructor<?>[] constructors = type.getConstructors();
                for(Constructor<?> constructor : constructors){
                    Object[] newParams = ConstructorUtils.validateInvocationAndConvertParams(constructor, params);
                    if(newParams != null){
                        result = (T) constructor.newInstance(newParams);
                        logger.trace("Successfully instantiated new instance of class {} with parameters {}", type.getName(), Arrays.toString(params));
                        break;
                    }
                }
            }
        }
        catch(InstantiationException | IllegalAccessException ex){
            throw new ReflectiveException("Unable to instantiate class: " + type.getName() + " with params: " + Arrays.toString(params), ex);
        }
        catch(InvocationTargetException ex){
            throw new InvocationException("Exception occurred while trying to instantiate class: " + type.getName() + " with params: " + Arrays.toString(params), ex);
        }

        //If result is still null, and no exceptions were thrown, then no matching constructor was found
        if(result == null){
            throw new ReflectiveException("No matching constructor found in class: " + type.getName() + " with params: " + Arrays.toString(params));
        }

        return result;
    }

}
