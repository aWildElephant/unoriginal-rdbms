package fr.awildelephant.rdbms.plan.aggregation;

public class SumAggregate implements Aggregate {

    private final String inputName;

    public SumAggregate(String inputName) {
        this.inputName = inputName;
    }

    public String inputName() {
        return inputName;
    }

    @Override
    public boolean outputIsNullable() {
        return true;
    }

    @Override
    public String outputName() {
        return "sum(" + inputName + ")";
    }

}
