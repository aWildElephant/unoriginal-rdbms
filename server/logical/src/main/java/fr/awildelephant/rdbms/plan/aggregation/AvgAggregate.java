package fr.awildelephant.rdbms.plan.aggregation;

import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;

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

    @Override
    public Domain outputType() {
        return DECIMAL;
    }
}
