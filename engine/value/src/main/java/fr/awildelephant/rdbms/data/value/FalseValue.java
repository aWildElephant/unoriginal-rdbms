package fr.awildelephant.rdbms.data.value;

public class FalseValue extends AbstractValue {

    private static final FalseValue FALSE = new FalseValue();

    public static FalseValue falseValue() {
        return FALSE;
    }

    @Override
    public boolean getBool() {
        return false;
    }
}
