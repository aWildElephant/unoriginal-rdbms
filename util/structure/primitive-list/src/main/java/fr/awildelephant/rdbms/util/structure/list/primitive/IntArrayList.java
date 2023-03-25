package fr.awildelephant.rdbms.util.structure.list.primitive;

import java.util.Arrays;

public class IntArrayList {

    private int[] backingArray;
    private int size;

    public IntArrayList(int initialCapacity) {
        backingArray = new int[initialCapacity];
        size = 0;
    }

    public int get(int index) {
        return backingArray[index];
    }

    public void set(int index, int value) {
        backingArray[index] = value;
    }

    public void add(int value) {
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
