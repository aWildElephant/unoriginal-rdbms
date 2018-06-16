package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.Table;
import fr.awildelephant.rdbms.plan.BaseTable;
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
        return projection.input().accept(this);
    }

    @Override
    public Table visit(BaseTable baseTable) {
        return tables.get(baseTable.name());
    }
}
