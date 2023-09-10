package fr.awildelephant.rdbms.storage.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.LongValue.longValue;

public class LongColumn extends ObjectColumn<Long> {

    public LongColumn(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    protected DomainValue transform(Long value) {
        return longValue(value);
    }

    @Override
    protected Long extract(DomainValue value) {
        return value.getLong();
    }
}
