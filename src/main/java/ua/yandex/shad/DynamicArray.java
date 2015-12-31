/*
 * @author Igor Tymchyshyn
 *
 * This class was written not only for concrete project,
 * it was written for possible using in future;
*/

package ua.yandex.shad;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DynamicArray<E> implements Collection<E> {

    private static final int DEFAULT_CAPACITY = 2;
    
    private E[] values;
    private int capacity;
    private int indexOfNextElem;
    
    public DynamicArray() {
        this.capacity = DEFAULT_CAPACITY;
        this.indexOfNextElem = 0;
        this.values = (E[]) (new Object[DEFAULT_CAPACITY]);
    }
    
    public DynamicArray(DynamicArray<E> array) {
        this.values = copyValues(array.values, DEFAULT_CAPACITY);
        this.capacity = this.values.length;
        this.indexOfNextElem = array.indexOfNextElem;
    }
    
    public DynamicArray(E ... array) {
        capacity = DEFAULT_CAPACITY;
        while (capacity < array.length) {
            capacity <<= 1;
        }
        this.values = copyValues(array, capacity);
        this.indexOfNextElem = array.length;
    }
    
    private E[] copyValues(E[] array, int minCapacity) {
        int valNumber = array.length;
        int size = valNumber;
        if (valNumber < minCapacity) {
            valNumber = minCapacity;
        }
        E[] valuesCopy = (E[]) (new Object[valNumber]);
        
        System.arraycopy(array, 0, valuesCopy, 0, size);
        
        return valuesCopy;
    }
    
    private void expand() {
        this.capacity <<= 1;
        E[] arrayExpanded = copyValues(this.values, this.capacity);
        values = arrayExpanded;
    }
    
    @Override
    public boolean add(E val) {
        if (val == null) {
            return false;
        }
        if (capacity == indexOfNextElem) {
            expand();
        }
        values[indexOfNextElem++] = val;
        return true;
    }
    
    public boolean add(E ... val) {
        if (val == null) {
            return false;
        }
        while (capacity < indexOfNextElem + val.length) {
            expand();
        }
        for (E v: val) {
            values[indexOfNextElem++] = v;
        }
        return true;
    }
    
    public boolean add(DynamicArray<E> array) {
        if (array == null) {
            return false;
        }
        E[] arrVal = (E[]) array.toArray();
        return add(arrVal);
    }
    
    
    /*
    public E[] getArray() {
        E[] array = (E[]) new Object[indexOfNextElem];
        System.arraycopy(values, 0, array, 0, indexOfNextElem);
        return array;
    }
    */
    public E getAt(int index) throws NoSuchElementException {
        if (index >= indexOfNextElem) {
            throw new NoSuchElementException();
        }
        return values[index];
    }
    
    public void changeAt(int index, E val) throws NoSuchElementException {
        if (index >= indexOfNextElem) {
            throw new NoSuchElementException();
        }
        values[index] = val;
    }
    
    public int removeAt(int index) throws NoSuchElementException {
        if (index >= indexOfNextElem) {
            throw new NoSuchElementException();
        }
        if (indexOfNextElem - 1 <= (capacity >> 2)) {
            E[] array = (E[]) (new Object[indexOfNextElem >> 1]);
            int j = 0;
            for (int i = 0; i < indexOfNextElem; i++) {
                if (i == index) {
                    continue;
                }
                array[j++] = values[i];
            }
            values = array;
            capacity = array.length;
            indexOfNextElem--;
        }
        else {
            for (int i = index + 1; i < indexOfNextElem; i++) {
                values[i-1] = values[i];
            }
            indexOfNextElem--;
        }
        return indexOfNextElem;
    }
    
    @Override
    public void clear() {
        values = (E[]) (new Object[DEFAULT_CAPACITY]);
        capacity = DEFAULT_CAPACITY;
        indexOfNextElem = 0;
    }
    
    @Override
    public int size() {
        return indexOfNextElem;
    }
    
    @Override
    public boolean isEmpty() {
        return indexOfNextElem == 0;
    }

    @Override
    public boolean contains(Object o) {
        E val = (E) o;
        for (int i = 0; i < indexOfNextElem; i++) {
            if (val.equals(values[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[indexOfNextElem];
        System.arraycopy(values, 0, array, 0, indexOfNextElem);
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] val = (T[]) Array.newInstance(a.getClass().getComponentType(), 
                                                indexOfNextElem);
        for (int i = 0; i < indexOfNextElem; i++) {
            val[i] = (T) values[i];
        }
        return val;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < indexOfNextElem; i++) {
            if (values[i].equals((E) o)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.stream().noneMatch((v) -> (!contains(v)));
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        E[] val = (E[]) (new Object[c.size()]);
        int i = 0;
        for (E e: c) {
            val[i++] = e;
        }
        return add(val);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean flag = true;
        for (Object o: c) {
            flag &= remove(o);
        }
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean flag = true;
        int j = 0;
        for (int i = 0; i < indexOfNextElem; i++) {
            if (!c.contains(values[i + j])) {
                removeAt(i + j);
                j--;
                flag = false;
            }
        }
        return flag;
    }
    
    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator();
    }
    
    private class ArrayIterator implements Iterator<E> {
        private int currentPos = 0;
        
        @Override
        public boolean hasNext() {
            return currentPos < indexOfNextElem;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return values[currentPos++];
        }
    }
     
}
