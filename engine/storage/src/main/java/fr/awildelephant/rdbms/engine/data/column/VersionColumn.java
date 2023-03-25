package fr.awildelephant.rdbms.engine.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.data.value.VersionValue;
import fr.awildelephant.rdbms.util.structure.list.primitive.LongArrayList;

import static fr.awildelephant.rdbms.data.value.VersionValue.versionValue;

public final class VersionColumn implements WriteableColumn {

    private final LongArrayList list;

    public VersionColumn(int initialCapacity) {
        this.list = new LongArrayList(initialCapacity);
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
    public VersionValue get(int index) {
        return versionValue(list.get(index));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void set(int index, DomainValue value) {
        list.set(index, value.getLong());
    }
}
