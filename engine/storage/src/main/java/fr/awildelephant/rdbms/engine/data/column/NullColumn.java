package fr.awildelephant.rdbms.engine.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public class NullColumn implements WriteableColumn {

    private int size = 0;

    @Override
    public DomainValue get(int index) {
        return nullValue();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(DomainValue value) {
        if (!value.isNull()) {
            throw new UnsupportedOperationException();
        }

        size++;
    }

    @Override
    public void ensureCapacity(int capacity) {

    }
}
