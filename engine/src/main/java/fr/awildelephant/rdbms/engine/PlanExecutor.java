package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.DistinctOperator;
import fr.awildelephant.rdbms.engine.operators.ProjectionOperator;
import fr.awildelephant.rdbms.plan.BaseTable;
import fr.awildelephant.rdbms.plan.Distinct;
import fr.awildelephant.rdbms.plan.PlanVisitor;
import fr.awildelephant.rdbms.plan.Projection;

import java.util.Map;

public class PlanExecutor implements PlanVisitor<Table> {

    private final Map<String, Table> tables;

    PlanExecutor(Map<String, Table> tables) {
        this.tables = tables;
    }

    @Override
    public Table visit(BaseTable baseTable) {
        return tables.get(baseTable.name());
    }

    @Override
    public Table visit(Distinct distinct) {
        return new DistinctOperator().compute(apply(distinct.input()));
    }

    @Override
    public Table visit(Projection projection) {
        return new ProjectionOperator(projection.schema()).compute(apply(projection.input()));
    }
}
