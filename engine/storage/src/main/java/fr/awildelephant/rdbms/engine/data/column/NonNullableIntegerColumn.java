package fr.awildelephant.rdbms.engine.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.Arrays;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;

public final class NonNullableIntegerColumn implements WriteableColumn {

    private int[] backingArray;
    private int size;

    public NonNullableIntegerColumn(int initialCapacity) {
        backingArray = new int[initialCapacity];
        size = 0;
    }

    @Override
    public DomainValue get(int index) {
        return integerValue(backingArray[index]);
    }

    @Override
    public void add(DomainValue value) {
        if (size == backingArray.length) {
            backingArray = Arrays.copyOf(backingArray, size + 1);
        }

        backingArray[size] = value.getInt();

        size++;
    }

    @Override
    public void ensureCapacity(int capacity) {
        if (capacity > backingArray.length) {
            backingArray = Arrays.copyOf(backingArray, capacity + 100000); // TODO: LOL
        }
    }

    @Override
    public int size() {
        return size;
    }
}
