package diyarraylist;

import java.util.*;
import java.util.function.Consumer;

public class DIYArrayList<T> implements List<T> {
    private Object[] array;
    private static final Object[] emptyArray = {};
    private int size;

    public DIYArrayList(int size) {
        this.array = new Object[size];
    }

    public DIYArrayList() {
        this.array = emptyArray;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return array.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (Object value : array) {
            if (value != null && value.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new DIYIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        if (size < array.length) {
            array[size] = t;
        } else if (size == 0) {
            array = new Object[10];
            array[size] = t;
        } else {
            Object[] extendedArray = Arrays.copyOf(array, array.length * 2);
            extendedArray[size] = t;
            this.array = extendedArray;
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (isIndexRangeValid(index)) {
            return (T) array[index];
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + " List size: " + this.size);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        if (isIndexRangeValid(index)) {
            T oldValue = (T) array[index];
            array[index] = element;
            return oldValue;
        }
        return null;
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DIYListIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new DIYListIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super T> comparator) {
        Arrays.sort((T[]) array, 0, size, comparator);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        throw new UnsupportedOperationException();
    }

    private boolean isIndexRangeValid(int idx){
        return idx >= 0 && idx < this.size;
    }


    private class DIYIterator implements Iterator<T> {
        int currentIdx;
        int lastReturned;

        private DIYIterator() {
        }

        @Override
        public boolean hasNext() {
            return currentIdx != size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if (currentIdx < size) {
                lastReturned = currentIdx++;
                return (T) array[lastReturned];
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    private class DIYListIterator extends DIYIterator implements ListIterator<T> {
        public DIYListIterator(int index) {
            super();
            currentIdx = index;
        }

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        @Override
        public T previous() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int nextIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            array[lastReturned] = t;
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }
}
