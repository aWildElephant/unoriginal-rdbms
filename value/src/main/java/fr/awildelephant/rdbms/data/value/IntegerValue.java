package fr.awildelephant.rdbms.data.value;

public final class IntegerValue extends AbstractValue {

    private final int value;

    private IntegerValue(int value) {
        this.value = value;
    }

    public static IntegerValue integerValue(int value) {
        return new IntegerValue(value);
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
