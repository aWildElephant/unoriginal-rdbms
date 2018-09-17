package fr.awildelephant.rdbms.plan.aggregation;

public class CountStarAggregate implements Aggregate {
    @Override
    public String outputName() {
        return "COUNT(*)";
    }
}
