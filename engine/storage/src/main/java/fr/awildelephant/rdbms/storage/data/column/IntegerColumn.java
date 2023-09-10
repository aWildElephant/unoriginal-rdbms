package fr.awildelephant.rdbms.storage.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;

public final class IntegerColumn extends ObjectColumn<Integer> {

    public IntegerColumn(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    protected DomainValue transform(Integer value) {
        return integerValue(value);
    }

    @Override
    protected Integer extract(DomainValue value) {
        return value.getInt();
    }
}
