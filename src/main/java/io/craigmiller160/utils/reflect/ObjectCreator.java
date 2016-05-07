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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
        T result = null;
        try{
            result = type.newInstance();
            logger.trace("Successfully instantiated new instance of class {}", type.getName());
        }
        catch(InstantiationException | IllegalAccessException ex){
            throw new ReflectiveException("Unable to instantiate class: " + type.getName(), ex);
        }
        return result;
    }

    public static <T> T instantiateClassWithParams(Class<T> type, Object...params) throws ReflectiveException{
        T result = null;

        try{
            Constructor<?> constructor = ConstructorUtils.findConstructor(type, params);
            if(constructor.isVarArgs()){
                params = ConstructorUtils.convertParamsForVarArgsConstructor(constructor, params);
            }
            result = (T) constructor.newInstance(params);
        }
        catch(InstantiationException | IllegalAccessException ex){
            throw new ReflectiveException("Unable to instantiate class: " + type.getName(), ex);
        }
        catch(InvocationTargetException ex){
            throw new InvocationException("Exception occurred while trying to instantiate class: " + type.getName(), ex);
        }

        return result;
    }

}
