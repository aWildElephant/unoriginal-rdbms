package fr.awildelephant.rdbms.engine.data.record;

import fr.awildelephant.rdbms.engine.data.value.DomainValue;

import java.util.Arrays;

public final class Record {

    private final DomainValue[] values;

    public Record(final DomainValue[] values) {
        this.values = values;
    }

    public DomainValue get(int attributeIndex) {
        return values[attributeIndex];
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Record)) {
            return false;
        }

        final Record other = (Record) obj;

        return Arrays.equals(values, other.values);
    }
}
