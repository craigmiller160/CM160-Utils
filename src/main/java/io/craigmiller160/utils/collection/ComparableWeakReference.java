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

package io.craigmiller160.utils.collection;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * A special implementation of WeakReference, designed to be
 * comparable. It provides special implementations of
 * equals, hashCode, and the comparable interface.
 *
 * Created by craig on 4/27/16.
 */
public class ComparableWeakReference<T> extends WeakReference<T> implements Comparable<ComparableWeakReference<T>> {

    public ComparableWeakReference(T referent) {
        super(referent);
    }

    public ComparableWeakReference(T referent, ReferenceQueue<? super T> q) {
        super(referent, q);
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){
           return false;
        }

        if(!(obj instanceof ComparableWeakReference)){
            return false;
        }

        ComparableWeakReference cwr = (ComparableWeakReference) obj;
        if(get() == null){
            return cwr.get() == null;
        }
        else{
            return cwr.get() != null && get().equals(cwr.get());
        }
    }

    @Override
    public int hashCode(){
        Object ref = get();
        return ref != null ? ref.hashCode() : 0;
    }

    @Override
    public int compareTo(ComparableWeakReference<T> o) {
        T thisRef = get();
        T otherRef = o.get();
        if(thisRef == null){
            return -1;
        }
        else if(otherRef == null){
            return 1;
        }
        else if(thisRef instanceof Comparable){
            return ((Comparable) thisRef).compareTo(otherRef);
        }
        else{
            return thisRef.toString().compareTo(otherRef.toString());
        }
    }
}
