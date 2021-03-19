package fr.awildelephant.rdbms.engine.data.record;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.Arrays;

public final class Tuple implements Record {

    public static final Tuple EMPTY_TUPLE = new Tuple(new DomainValue[0]);

    private final DomainValue[] values;

    public Tuple(final DomainValue[] values) {
        this.values = values;
    }

    @Override
    public DomainValue get(int attributeIndex) {
        return values[attributeIndex];
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public Record materialize() {
        return this;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tuple)) {
            return false;
        }

        final Tuple other = (Tuple) obj;

        return Arrays.equals(values, other.values);
    }
}
