package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.Table;
import fr.awildelephant.rdbms.engine.operators.ProjectionOperator;
import fr.awildelephant.rdbms.plan.BaseTable;
import fr.awildelephant.rdbms.plan.Plan;
import fr.awildelephant.rdbms.plan.PlanVisitor;
import fr.awildelephant.rdbms.plan.Projection;

import java.util.Map;

public class PlanExecutor implements PlanVisitor<Table> {

    private final Map<String, Table> tables;

    PlanExecutor(Map<String, Table> tables) {
        this.tables = tables;
    }

    @Override
    public Table visit(Projection projection) {
        final Plan input = projection.input();
        final ProjectionOperator projectionOperator = new ProjectionOperator(projection.schema());

        return projectionOperator.compute(input.accept(this));
    }

    @Override
    public Table visit(BaseTable baseTable) {
        return tables.get(baseTable.name());
    }
}
