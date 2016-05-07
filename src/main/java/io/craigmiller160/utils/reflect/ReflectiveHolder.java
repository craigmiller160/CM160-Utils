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

import java.lang.reflect.AccessibleObject;

/**
 * Abstract superclass of an object that can
 * hold a reflective component and the source it
 * comes from. ie, a Method and its corresponding Object,
 * or a Constructor and its corresponding Class, etc.
 *
 * Created by craig on 5/7/16.
 */
public abstract class ReflectiveHolder<T,U extends AccessibleObject> {

    private final T source;
    private final U reflectiveComponent;

    protected ReflectiveHolder(T source, U reflectiveComponent){
        this.source = source;
        this.reflectiveComponent = reflectiveComponent;
    }

    public T getSource(){
        return source;
    }

    public U getReflectiveComponent(){
        return reflectiveComponent;
    }

    public abstract Class<?> getSourceType();

    public boolean isReflectiveComponentAccessible(){
        return reflectiveComponent.isAccessible();
    }

}
