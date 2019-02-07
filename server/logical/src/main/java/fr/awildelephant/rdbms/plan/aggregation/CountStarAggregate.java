package fr.awildelephant.rdbms.plan.aggregation;

public class CountStarAggregate implements Aggregate {

    @Override
    public boolean outputIsNullable() {
        return false;
    }

    @Override
    public String outputName() {
        return "count(*)";
    }
}
