package fr.awildelephant.rdbms.storage.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.ArrayList;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public abstract class ObjectColumn<T> implements AppendableColumn {

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

    public T getGeneric(int index) {
        return backingArray.get(index);
    }

    protected abstract DomainValue transform(T value);

    @Override
    public void add(final DomainValue value) {
        if (value.isNull()) {
            backingArray.add(null);
        } else {
            backingArray.add(extract(value));
        }
    }

    public void addGeneric(final T value) {
        backingArray.add(value);
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
