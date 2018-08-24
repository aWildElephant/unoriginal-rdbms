package fr.awildelephant.rdbms.data.value;

public final class NullValue extends AbstractValue {

    public static NullValue NULL_VALUE = new NullValue();

    private NullValue() {

    }

    @Override
    public boolean isNull() {
        return true;
    }
}
