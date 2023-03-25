package fr.awildelephant.rdbms.data.value;

public final class TrueValue implements DomainValue {

    private static final TrueValue TRUE_VALUE = new TrueValue();

    public static TrueValue trueValue() {
        return TRUE_VALUE;
    }

    @Override
    public boolean getBool() {
        return true;
    }

    @Override
    public String toString() {
        return "TrueValue";
    }
}
