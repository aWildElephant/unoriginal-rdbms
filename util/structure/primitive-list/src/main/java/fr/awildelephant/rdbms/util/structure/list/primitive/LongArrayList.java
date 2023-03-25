package fr.awildelephant.rdbms.util.structure.list.primitive;

import java.util.Arrays;

public class LongArrayList {

    private long[] backingArray;
    private int size;

    public LongArrayList(int initialCapacity) {
        backingArray = new long[initialCapacity];
        size = 0;
    }

    public long get(int index) {
        return backingArray[index];
    }

    public void set(int index, long value) {
        backingArray[index] = value;
    }

    public void add(long value) {
        if (backingArray.length <= size) {
            ensureCapacity(size + 1);
        }

        backingArray[size] = value;

        size++;
    }

    public int size() {
        return size;
    }

    public void ensureCapacity(int capacity) {
        if (capacity > backingArray.length) {
            // TODO: something less stupid
            backingArray = Arrays.copyOf(backingArray, capacity + 100000);
        }
    }
}
