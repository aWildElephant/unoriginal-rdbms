package fr.awildelephant.rdbms.data.value;

public final class FalseValue implements DomainValue {

    private static final FalseValue FALSE = new FalseValue();

    public static FalseValue falseValue() {
        return FALSE;
    }

    @Override
    public boolean getBool() {
        return false;
    }

    @Override
    public String toString() {
        return "FalseValue";
    }
}
