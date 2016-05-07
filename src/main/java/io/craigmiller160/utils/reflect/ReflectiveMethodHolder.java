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

import java.lang.reflect.Method;

/**
 * Abstract superclass of the reflective method holder
 * classes.
 *
 * Created by craigmiller on 3/19/16.
 */
public abstract class ReflectiveMethodHolder<T> extends ReflectiveHolder<T,Method> implements ParameterizedHolder{

    private final Class<?>[] paramTypes;

    protected ReflectiveMethodHolder(T source, Method method){
        super(source, method);
        this.paramTypes = method.getParameterTypes();
    }

    @Override
    public Class<?>[] getParamTypes(){
        return paramTypes;
    }

    @Override
    public int getParamCount(){
        return paramTypes.length;
    }

    @Override
    public boolean isVarArgs(){
        return getReflectiveComponent().isVarArgs();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReflectiveMethodHolder<?> that = (ReflectiveMethodHolder<?>) o;

        if (getSource() != null ? !getSource().equals(that.getSource()) : that.getSource() != null) return false;
        return getReflectiveComponent() != null ? getReflectiveComponent().equals(that.getReflectiveComponent()) : that.getReflectiveComponent() == null;

    }

    @Override
    public int hashCode() {
        int result = getSource() != null ? getSource().hashCode() : 0;
        result = 31 * result + (getReflectiveComponent() != null ? getReflectiveComponent().hashCode() : 0);
        return result;
    }
}
