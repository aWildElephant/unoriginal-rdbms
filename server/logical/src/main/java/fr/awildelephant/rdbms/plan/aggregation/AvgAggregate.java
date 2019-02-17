package fr.awildelephant.rdbms.plan.aggregation;

public class AvgAggregate implements Aggregate {

    private final String inputName;

    public AvgAggregate(String inputName) {
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
        return "avg(" + inputName + ")";
    }
}
