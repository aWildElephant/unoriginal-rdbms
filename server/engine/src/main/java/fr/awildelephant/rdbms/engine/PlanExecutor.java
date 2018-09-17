package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.TableWithChecker;
import fr.awildelephant.rdbms.engine.operators.AggregationOperator;
import fr.awildelephant.rdbms.engine.operators.AliasOperator;
import fr.awildelephant.rdbms.engine.operators.DistinctOperator;
import fr.awildelephant.rdbms.engine.operators.MapOperator;
import fr.awildelephant.rdbms.engine.operators.ProjectionOperator;
import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.LopVisitor;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;

import java.util.Map;

public class PlanExecutor implements LopVisitor<Table> {

    private final Map<String, TableWithChecker> tables;

    PlanExecutor(Map<String, TableWithChecker> tables) {
        this.tables = tables;
    }

    @Override
    public Table visit(AggregationLop aggregationNode) {
        return new AggregationOperator(aggregationNode.schema()).compute(apply(aggregationNode.input()));
    }

    @Override
    public Table visit(AliasLop aliasNode) {
        return new AliasOperator(aliasNode.schema()).compute(apply(aliasNode.input()));
    }

    @Override
    public Table visit(BaseTableLop baseTable) {
        return tables.get(baseTable.name());
    }

    @Override
    public Table visit(DistinctLop distinctNode) {
        return new DistinctOperator().compute(apply(distinctNode.input()));
    }

    @Override
    public Table visit(MapLop mapNode) {
        return new MapOperator(mapNode.operations(), mapNode.schema()).compute(apply(mapNode.input()));
    }

    @Override
    public Table visit(ProjectionLop projectionNode) {
        return new ProjectionOperator(projectionNode.schema()).compute(apply(projectionNode.input()));
    }
}
