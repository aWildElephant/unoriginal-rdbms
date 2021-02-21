package fr.awildelephant.rdbms.data.value;

public final class NullValue extends AbstractValue {

    private static final NullValue NULL_VALUE = new NullValue();

    private NullValue() {

    }

    public static NullValue nullValue() {
        return NULL_VALUE;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public String toString() {
        return "NullValue";
    }
}
