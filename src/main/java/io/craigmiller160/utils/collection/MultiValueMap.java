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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A special Map implementation that has multiple
 * values for all keys.
 *
 * Created by Craig on 2/10/2016.
 */
public class MultiValueMap<K,V> implements Map<K,Collection<V>> {

    /**
     * The underlying map within this special map implementation.
     */
    private Map<K,Collection<V>> map;

    /**
     * A separate list used to store all the values
     * added to this map.
     */
    private Set<V> allValues;

    /**
     * An integer value for the full size of
     * all the collections of this map.
     */
    private int fullSize = 0;

    public MultiValueMap(){
        map = new HashMap<>();
        allValues = new HashSet<>();
    }

    /**
     * Factory method that builds a new collection
     * to store the values of this map.
     *
     * Subclasses can and should override this class
     * to use different collection types to achieve
     * different behaviors and results.
     *
     * By default, it returns an ArrayList.
     *
     * @return the collection being used to store
     * values in this Map.
     */
    protected Collection<V> getNewCollection(){
        return new ArrayList<>();
    }

    /**
     * Get a count of the number of collections
     * in the map.
     *
     * @return the number of collections in the Map.
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Get a count of the total number of values
     * in all the collections in the Map.
     *
     * @return the total number of values in all collections
     *          in the Map.
     */
    public int fullSize(){
//        Set<K> keySet = map.keySet();
//        int total = 0;
//        for(K k : keySet){
//            total += map.get(k).size();
//        }
//        return total;
        return fullSize;
    }

    /**
     * Get the size of the collection
     * for the specified key.
     *
     * @param key the key of the collection
     *            to get the size of.
     * @return the size of the collection
     * matching the key, or -1 if there is
     * no collection matching the key.
     */
    public int valueSize(K key){
        Collection<V> c = map.get(key);
        if(c != null){
            return c.size();
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    /**
     * Test if this Map contains the provided value,
     * in any of the collections it contains.
     *
     * @param value the value to test if this Map contains.
     * @return true if the value is within any of the Map's collections.
     */
    @Override
    public boolean containsValue(Object value) {
//        Set<K> keySet = map.keySet();
//        for(K k : keySet){
//            Collection<V> c = map.get(k);
//            if(c.contains(value)){
//                return true;
//            }
//        }
//        return false;
        return allValues.contains(value);
    }

    /**
     * Test if the Map contains a collection that matches
     * the one provided.
     *
     * @param c the colleciton to test if the Map contains.
     * @return true if the map contains a matching collection.
     */
    public boolean containsCollection(Collection<V> c){
        return map.containsValue(c);
    }

    @Override
    public Collection<V> get(Object key) {
        return map.get(key);
    }

    @Override
    public Collection<V> put(K key, Collection<V> c) {
        Collection<V> values = map.get(key);
        if(values != null){
            values.addAll(c);
        }
        else{
            map.put(key, c);
        }

        allValues.addAll(c);
        fullSize += c.size();

        return c;
    }

    public V putValue(K key, V value){
        Collection<V> values = map.get(key);
        if(values == null){
            values = getNewCollection();
            map.put(key, values);
        }
        values.add(value);
        allValues.add(value);
        fullSize += 1;
        return value;
    }

    public V putValueInMultipleCollections(Collection<K> keys, V value){
        for(K key : keys){
            putValue(key, value);
        }

        return value;
    }

    public Collection<V> putMultipleValuesIntoCollection(K key, Collection<V> values){
        Collection<V> collection = map.get(key);
        if(collection == null){
            collection = getNewCollection();
        }
        collection.addAll(values);
        return put(key, collection);
    }

    @Override
    public Collection<V> remove(Object key) {
        Collection<V> toRemove = map.remove(key);
        allValues.removeAll(toRemove);
        fullSize -= toRemove.size();
        return toRemove;
    }

    /**
     * Remove a value form any collections that contain
     * it in this map.
     *
     * @param value the value to remove from the collection.
     * @return the value that has been removed.
     */
    public V removeValue(V value){
        Set<K> keySet = keySet();
        List<K> keysToRemove = new ArrayList<>();
        for(K k : keySet){
            Collection<V> c = map.get(k);
            if(c != null){
                if(c.remove(value)){
                    fullSize -= 1;
                }

                if(c.size() == 0){
                    keysToRemove.add(k);
                }
            }
        }

        allValues.remove(value);

        for(K k : keysToRemove){
            remove(k);
        }

        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends Collection<V>> m) {
        Set<? extends K> keySet = m.keySet();
        Collection<V> template = getNewCollection();
        for(K k : keySet){
            Collection c = m.get(k);
            if(c != null){
                put(k,c);
            }
        }
    }

    @Override
    public void clear() {
        map.clear();
        allValues.clear();
        fullSize = 0;
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Collection<V>> values() {
        return map.values();
    }

    /**
     * Get all the values of all the collections of this
     * map, returned in a list so that duplicates will
     * be preserved.
     *
     * @return a list of all the values of all the collections.
     */
    public Set<V> allValues(){
//        List<V> result = new ArrayList<>();
//        for(Collection<V> value : values()){
//            result.addAll(value);
//        }

        return allValues;
    }

    @Override
    public Set<Entry<K, Collection<V>>> entrySet() {
        return map.entrySet();
    }
}
