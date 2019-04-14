package fr.awildelephant.rdbms.plan.aggregation;

import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;

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

    // TODO: this is INTEGER if the input column is integer
    @Override
    public Domain outputType() {
        return DECIMAL;
    }
}
