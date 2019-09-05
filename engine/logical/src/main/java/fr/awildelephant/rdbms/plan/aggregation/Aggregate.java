package fr.awildelephant.rdbms.plan.aggregation;

public interface Aggregate {

    boolean outputIsNullable();

    String outputName();
}
