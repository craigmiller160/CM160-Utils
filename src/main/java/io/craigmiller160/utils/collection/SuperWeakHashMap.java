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

package io.craigmiller160.utils.collection;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A special enhanced version of the WeakHashMap. This map
 * has both keys and values as WeakReferences, and an entry
 * will be removed if either the key or value has become
 * null due to all other references no longer existing.
 *
 * Created by craig on 4/25/16.
 */
public class SuperWeakHashMap<K,V> implements Map<K,V> {

    private final Map<ComparableWeakReference<K>, ComparableWeakReference<V>> internalMap = new HashMap<>();

    public SuperWeakHashMap(){

    }

    /**
     * Clear any stale references to keys and values and return a map
     * of just the valid entries.
     *
     * @return the map of valid entries.
     */
    private Map<K,V> clearStaleAndGetInternalMap(){
        Map<K,V> result = new HashMap<>();
        Iterator<Map.Entry<ComparableWeakReference<K>,ComparableWeakReference<V>>> it = internalMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<ComparableWeakReference<K>,ComparableWeakReference<V>> entry = it.next();
            if(entry.getKey() == null || entry.getKey().get() == null){
                it.remove();
            }
            else if(entry.getValue() == null || entry.getValue().get() == null){
                it.remove();
            }
            else{
                result.put(entry.getKey().get(), entry.getValue().get());
            }
        }

        return result;
    }

    @Override
    public int size() {
        return clearStaleAndGetInternalMap().size();
    }

    @Override
    public boolean isEmpty() {
        return clearStaleAndGetInternalMap().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return clearStaleAndGetInternalMap().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return clearStaleAndGetInternalMap().containsValue(value);
    }

    @Override
    public V get(Object key) {
        return clearStaleAndGetInternalMap().get(key);
    }

    @Override
    public V put(K key, V value) {
        //Do this directly, because the indirect way won't work
        internalMap.put(new ComparableWeakReference<>(key), new ComparableWeakReference<>(value));
        return value;
    }

    @Override
    public V remove(Object key) {
        //Do this directly, because it's not worth the iteration for a removal
        ComparableWeakReference<V> result = null;
        //Test for just WeakReference, because both WeakReference and ComparableWeakReference should have the same behavior here.
        if(key instanceof WeakReference){
            result = internalMap.remove(key);
        }
        else{
            result = internalMap.remove(new ComparableWeakReference(key));
        }

        return result != null ? result.get() : null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Entry<? extends K,? extends V> entry : m.entrySet()){
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        //Do this directly because it's not worth the iteration if everything is being cleared.
        internalMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return clearStaleAndGetInternalMap().keySet();
    }

    @Override
    public Collection<V> values() {
        return clearStaleAndGetInternalMap().values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return clearStaleAndGetInternalMap().entrySet();
    }
}
