package fr.awildelephant.rdbms.storage.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.Arrays;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;

// TODO: we could store 4 nullable boolean values in a byte, 8 non-nullable boolean values
public final class BooleanColumn implements AppendableColumn {

    private byte[] backingArray;
    private int size;

    public BooleanColumn(int initialCapacity) {
        backingArray = new byte[initialCapacity];
        size = 0;
    }

    @Override
    public DomainValue get(int index) {
        final byte value = backingArray[index];

        if (value == 0) {
            return falseValue();
        } else if (value == 1) {
            return trueValue();
        } else {
            return nullValue();
        }
    }

    @Override
    public void add(DomainValue value) {
        if (size == backingArray.length) {
            backingArray = Arrays.copyOf(backingArray, size + 1);
        }

        if (value.isNull()) {
            backingArray[size] = 2;
        } else if (value.getBool()) {
            backingArray[size] = 1;
        } else {
            backingArray[size] = 0;
        }

        size++;
    }

    @Override
    public void ensureCapacity(int capacity) {
        if (capacity > backingArray.length) {
            backingArray = Arrays.copyOf(backingArray, capacity);
        }
    }

    @Override
    public int size() {
        return size;
    }
}
