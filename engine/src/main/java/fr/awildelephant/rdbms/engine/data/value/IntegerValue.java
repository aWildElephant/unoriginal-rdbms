package fr.awildelephant.rdbms.engine.data.value;

public final class IntegerValue extends AbstractValue {

    private final int value;

    public IntegerValue(int value) {
        this.value = value;
    }

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntegerValue)) {
            return false;
        }

        final IntegerValue other = (IntegerValue) obj;

        return value == other.value;
    }
}
