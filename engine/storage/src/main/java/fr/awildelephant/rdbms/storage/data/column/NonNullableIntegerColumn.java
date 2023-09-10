package fr.awildelephant.rdbms.storage.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.util.structure.list.primitive.IntArrayList;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;

public final class NonNullableIntegerColumn implements AppendableColumn {

    private final IntArrayList list;

    public NonNullableIntegerColumn(int initialCapacity) {
        list = new IntArrayList(initialCapacity);
    }

    @Override
    public DomainValue get(int index) {
        return integerValue(list.get(index));
    }

    @Override
    public void add(DomainValue value) {
        list.add(value.getInt());
    }

    @Override
    public void ensureCapacity(int capacity) {
        list.ensureCapacity(capacity);
    }

    @Override
    public int size() {
        return list.size();
    }
}
