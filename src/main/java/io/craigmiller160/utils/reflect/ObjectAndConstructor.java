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

import java.lang.reflect.Constructor;

/**
 * Created by craig on 5/7/16.
 */
public class ObjectAndConstructor extends ReflectiveHolder<Object,Constructor> implements ParameterizedHolder{

    public ObjectAndConstructor(Object object, Constructor constructor){
        super(object, constructor);
    }

    @Override
    public Class<?> getSourceType() {
        return getSource().getClass();
    }

    @Override
    public Class<?>[] getParamTypes() {
        return getReflectiveComponent().getParameterTypes();
    }

    @Override
    public int getParamCount() {
        return getReflectiveComponent().getParameterTypes().length;
    }

    @Override
    public boolean isVarArgs() {
        return getReflectiveComponent().isVarArgs();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectAndConstructor that = (ObjectAndConstructor) o;

        if (getSource() != null ? !getSource().equals(that.getSource()) : that.getSource() != null) return false;
        return getReflectiveComponent() != null ? getReflectiveComponent().equals(that.getReflectiveComponent()) : that.getReflectiveComponent() == null;

    }

    @Override
    public int hashCode() {
        int result = getSource() != null ? getSource().hashCode() : 0;
        result = 31 * result + (getReflectiveComponent() != null ? getReflectiveComponent().hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        String className = getSource().getClass().getName();
        String[] paramTypeNames = getParamTypeNames();

        StringBuilder builder = new StringBuilder()
                .append(className)
                .append(".")
                .append("<init>")
                .append("(");

        for(int i = 0; i < paramTypeNames.length; i++){
            builder.append(paramTypeNames[i]);
            if(i < paramTypeNames.length - 1){
                builder.append(",");
            }
        }
        builder.append(")");

        return builder.toString();
    }

    private String[] getParamTypeNames(){
        String[] paramTypeNames = new String[getParamTypes().length];
        for(int i = 0; i < paramTypeNames.length; i++){
            paramTypeNames[i] = getParamTypes()[i].getName();
        }
        return paramTypeNames;
    }
}
