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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A special implementation of the List interface
 * that maintains a sort order at all times. The side
 * effects of this are inconsistent index values (positions
 * are constantly changing every time the contents of
 * the list changes) and the inability to insert elements
 * at specific positions. However, if a sorted collection
 * that can be accessed by index number is needed, this
 * is a great option.
 *
 * The sort order is maintained by a comparator. By default,
 * the comparator used checks if the values in the list
 * implement Comparable, and uses compareTo(...) if they do.
 * If they don't, it compares the toString() values of
 * the items in the collection. However, this is only used
 * if a custom comparator is not provided, and it is very
 * recommended to do so based on how this list is going
 * to be used.
 *
 * Created by craigmiller on 4/19/16.
 */
public class SortedList<T> extends AbstractList<T> {

    /**
     * An underlying ArrayList that this class wraps around.
     */
    private List<T> list;

    /**
     * A comparator that, when it is provided, is used to
     * define the sort order of the collection.
     */
    private Comparator<T> comparator;

    /**
     * Create a new SortedList with a default
     * comparator to maintain its sort order.
     */
    public SortedList(){
        this(null, null);
    }

    /**
     * Create a new SortedList with a default
     * comparator to maintain its sort order. The elements
     * of the provided collection will be added to the
     * list in sorted order.
     *
     * @param collection the Collection to add to the list.
     */
    public SortedList(Collection<T> collection){
        this(collection, null);
    }

    /**
     * Create a new SortedList, with its sort order
     * enforced by the provided Comparator.
     *
     * @param comparator the Comparator to enforce the sort order.
     */
    public SortedList(Comparator<T> comparator){
        this(null, comparator);
    }

    /**
     * Create a new SortedList, with its sort order
     * enforced by the provided Comparator. The elements
     * of the provided collection will be added to the
     * list in sorted order.
     *
     * @param collection the Collection to add to the list.
     * @param comparator the Comparator to enforce the sort order.
     */
    public SortedList(Collection<T> collection, Comparator<T> comparator){
        this.list = new ArrayList<>();
        setComparator(comparator);

        if(collection != null){
            addAll(collection);
        }
    }

    /**
     * Set the Comparator to use to enforce the sort order of this list.
     *
     * @param comparator the Comparator.
     */
    public void setComparator(Comparator<T> comparator){
        if(comparator != null){
            this.comparator = comparator;
        }
        else{
            this.comparator = new DefaultComparator();
        }

        Collections.sort(list, this.comparator);
    }

    /**
     * Get the comparator that enforces the sort
     * order of this list.
     *
     * @return the Comparator.
     */
    public Comparator<T> getComparator(){
        return comparator;
    }

    /**
     * This is a special and powerful method that bypasses
     * the sorting process of this collection and simply
     * places the contents of the list into the underlying
     * list unchanged. This should only be done if you are
     * certain that the list being set is already in the
     * same sort order that this collection already maintains.
     *
     * The main use case for this method is when retrieving a
     * large amount of pre-sorted data that will then be
     * dumped into this list. In that case, the extra sorting
     * would hinder performance, and is only valuable for maintaining
     * the order while the data is being manipulated.
     *
     * Because this method could break the contract of this class,
     * namely that all items will always maintain the specified
     * sort order, it should be used very carefully and very
     * sparingly.
     *
     * Lastly, when invoked, all previous entries in the list will
     * be erased and the new data will replace it.
     *
     * @param preSortedList the pre-sorted list to add
     */
    public void setPreSortedList(List<T> preSortedList){
        if(preSortedList != null){
            this.list = preSortedList;
        }
        else{
            this.list = new ArrayList<>();
        }
    }

    /**
     * This method is redundant in this class, as items
     * are placed in the List at locations according
     * to their orders. As a result, this method just
     * called the standard add(T) method and ignores
     * the location argument.
     *
     * @param location the location argument to be ignored.
     * @param object the object to add to the collection.
     */
    @Override
    public void add(int location, T object) {
        add(object);
    }

    /**
     * Add the provided object to this List. Its
     * placement in the list will be determined
     * by the sort order.
     *
     * @param object the object to add to the list.
     * @return true if it's added successfully.
     */
    @Override
    public boolean add(T object) {
        int position = getSortedPosition(object);
        list.add(position, object);
        return true;
    }

    /**
     * Internal utility method to get the position
     * of an object based on the sort order. It returns
     * the position the object either does currently have
     * or the position it should have after being added.
     *
     * @param object the object to get the position of.
     * @return the position the object should have.
     */
    private int getSortedPosition(T object){
        int result = Collections.binarySearch(list, object, comparator);
        if (result < 0) {
            return (result + 1) * -1;
        }
        return result;
    }

    /**
     * This method is redundant in this class, as items
     * are placed in the List at locations according
     * to their orders. As a result, this method just
     * called the standard addAll(Collection) method and ignores
     * the location argument.
     *
     * @param location the location argument to be ignored.
     * @param collection the collection to add to this collection.
     */
    @Override
    public boolean addAll(int location, Collection<? extends T> collection) {
        return addAll(collection);
    }

    /**
     * Add all elements in this collection to the list.
     * They will be sorted to maintain the sort order.
     *
     * @param collection the collection to add to the list.
     * @return true if the operation succeeds.
     */
    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean result = list.addAll(collection);
        if(result){
            Collections.sort(list, comparator);
        }
        return result;
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean contains(Object object) {
        return list.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return list.containsAll(collection);
    }

    /**
     * {@inheritDoc}
     *
     * NOTE: Because this list's order changes dynamically
     * to maintain its sort order, location positions are not
     * guaranteed over time.
     *
     * @param location the location to get the element from.
     * @return the element at that location.
     */
    @Override
    public T get(int location) {
        return list.get(location);
    }

    /**
     * {@inheritDoc}
     *
     * NOTE: Because this list's order changes dynamically
     * to maintain its sort order, location positions are not
     * guaranteed over time.
     *
     * @param object the object to get the position of.
     * @return the position of that object.
     */
    @Override
    public int indexOf(Object object) {
        return list.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * {@inheritDoc}
     *
     * NOTE: This iterator will respect the sort order
     * of this list.
     *
     * @return the Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return super.iterator();
    }

    /**
     * {@inheritDoc}
     *
     * NOTE: Because this list's order changes dynamically
     * to maintain its sort order, location positions are not
     * guaranteed over time.
     *
     * @param object
     * @return
     */
    @Override
    public int lastIndexOf(Object object) {
        return list.lastIndexOf(object);
    }

    /**
     * {@inheritDoc}
     *
     * NOTE: This iterator will respect the sort order
     * of this list.
     *
     * @return the ListIterator.
     */
    @Override
    public ListIterator<T> listIterator() {
        return super.listIterator();
    }

    /**
     * {@inheritDoc}
     *
     * NOTE: This iterator will respect the sort order
     * of this list.
     *
     * @param location the location to start the iteration at.
     * @return the ListIterator.
     */
    @Override
    public ListIterator<T> listIterator(int location) {
        return super.listIterator(location);
    }

    @Override
    public T remove(int location) {
        return list.remove(location);
    }

    @Override
    public boolean remove(Object object) {
        return list.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return list.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return list.retainAll(collection);
    }

    /**
     * Replace the object at the specified location
     * with the provided one. This method still respects
     * the sort order of the list, so while the object
     * previously at the specified location will be
     * removed, the object added will be placed in the
     * list at the appropriate location based on the sort
     * order.
     *
     * @param location the location to replace the object at.
     * @param object the object to put into the list.
     * @return the object that was placed into the list.
     */
    @Override
    public T set(int location, T object) {
        list.remove(location);
        add(object);
        return object;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public List<T> subList(int start, int end) {
        return list.subList(start, end);
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] array) {
        return list.toArray(array);
    }

    /**
     * The default comparator for a SortedList, if
     * no other one is provided.
     */
    private class DefaultComparator implements Comparator<T>{

        @Override
        public int compare(Object o1, Object o2) {
            if(o1 instanceof Comparable && o2 instanceof Comparable){
                return ((Comparable) o1).compareTo(o2);
            }
            return o1.toString().compareTo(o2.toString());
        }
    }

}
