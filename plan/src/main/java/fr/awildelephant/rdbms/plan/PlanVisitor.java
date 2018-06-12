package fr.awildelephant.rdbms.plan;

public interface PlanVisitor<T> {

    T visit(Projection projection);

    T visit(BaseTable baseTable);
}
