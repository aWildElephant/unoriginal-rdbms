package fr.awildelephant.rdbms.plan.aggregation;

public class MinAggregate implements Aggregate {

    private final String inputName;

    public MinAggregate(String inputName) {
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
        return "min(" + inputName + ")";
    }

}
