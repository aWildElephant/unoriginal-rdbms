package fr.awildelephant.rdbms.plan.aggregation;

import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public class CountStarAggregate implements Aggregate {

    @Override
    public boolean outputIsNullable() {
        return false;
    }

    @Override
    public String outputName() {
        return "count(*)";
    }

    @Override
    public Domain outputType() {
        return INTEGER;
    }
}
