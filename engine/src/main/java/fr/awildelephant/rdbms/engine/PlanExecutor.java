package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.TableWithChecker;
import fr.awildelephant.rdbms.engine.operators.AliasOperator;
import fr.awildelephant.rdbms.engine.operators.DistinctOperator;
import fr.awildelephant.rdbms.engine.operators.ProjectionOperator;
import fr.awildelephant.rdbms.plan.AliasNode;
import fr.awildelephant.rdbms.plan.BaseTable;
import fr.awildelephant.rdbms.plan.DistinctNode;
import fr.awildelephant.rdbms.plan.PlanVisitor;
import fr.awildelephant.rdbms.plan.ProjectionNode;

import java.util.Map;

public class PlanExecutor implements PlanVisitor<Table> {

    private final Map<String, TableWithChecker> tables;

    PlanExecutor(Map<String, TableWithChecker> tables) {
        this.tables = tables;
    }

    @Override
    public Table visit(AliasNode aliasNode) {
        return new AliasOperator(aliasNode.schema()).compute(apply(aliasNode.input()));
    }

    @Override
    public Table visit(BaseTable baseTable) {
        return tables.get(baseTable.name());
    }

    @Override
    public Table visit(DistinctNode distinctNode) {
        return new DistinctOperator().compute(apply(distinctNode.input()));
    }

    @Override
    public Table visit(ProjectionNode projectionNode) {
        return new ProjectionOperator(projectionNode.schema()).compute(apply(projectionNode.input()));
    }
}
