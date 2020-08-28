package fr.awildelephant.rdbms.engine.data.record;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.Arrays;

public final class Record {

    public static final Record EMPTY_RECORD = new Record(new DomainValue[0]);

    private final DomainValue[] values;

    public Record(final DomainValue[] values) {
        this.values = values;
    }

    public DomainValue get(int attributeIndex) {
        return values[attributeIndex];
    }

    public int size() {
        return values.length;
    }

    public boolean anyNull() {
        for (DomainValue value : values) {
            if (value.isNull()) {
                return true;
            }
        }

        return false;
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
