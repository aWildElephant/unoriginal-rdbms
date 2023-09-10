package fr.awildelephant.rdbms.storage.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.util.structure.list.primitive.LongArrayList;

import static fr.awildelephant.rdbms.data.value.LongValue.longValue;

public class NonNullableLongColumn implements AppendableColumn {

    private final LongArrayList list;

    public NonNullableLongColumn(int initialCapacity) {
        list = new LongArrayList(initialCapacity);
    }

    @Override
    public DomainValue get(int index) {
        return longValue(list.get(index));
    }

    @Override
    public void add(DomainValue value) {
        list.add(value.getLong());
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
