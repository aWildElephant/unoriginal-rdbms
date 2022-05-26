package fr.awildelephant.rdbms.engine.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.ArrayList;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public abstract class ObjectColumn<T> implements WriteableColumn {

    protected final ArrayList<T> backingArray;

    protected ObjectColumn(int initialCapacity) {
        backingArray = new ArrayList<>(initialCapacity);
    }

    @Override
    public DomainValue get(int index) {
        final T value = backingArray.get(index);

        if (value == null) {
            return nullValue();
        }

        return transform(value);
    }

    protected abstract DomainValue transform(T value);

    @Override
    public void add(DomainValue value) {
        if (value.isNull()) {
            backingArray.add(null);
        } else {
            backingArray.add(extract(value));
        }
    }

    protected abstract T extract(DomainValue value);

    @Override
    public void ensureCapacity(int capacity) {
        backingArray.ensureCapacity(capacity);
    }

    @Override
    public int size() {
        return backingArray.size();
    }
}
