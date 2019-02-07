package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.TableWithChecker;
import fr.awildelephant.rdbms.engine.operators.AggregationOperator;
import fr.awildelephant.rdbms.engine.operators.AliasOperator;
import fr.awildelephant.rdbms.engine.operators.BreakdownOperator;
import fr.awildelephant.rdbms.engine.operators.DistinctOperator;
import fr.awildelephant.rdbms.engine.operators.MapOperator;
import fr.awildelephant.rdbms.engine.operators.ProjectionOperator;
import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.BreakdownLop;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.LopVisitor;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;

import java.util.Map;
import java.util.stream.Stream;

public class PlanExecutor implements LopVisitor<Stream<Table>> {

    private final Map<String, TableWithChecker> tables;

    PlanExecutor(Map<String, TableWithChecker> tables) {
        this.tables = tables;
    }

    @Override
    public Stream<Table> visit(AggregationLop aggregationNode) {
        final AggregationOperator operator = new AggregationOperator(aggregationNode.aggregates(), aggregationNode.schema());

        return apply(aggregationNode.input()).map(operator::compute);
    }

    @Override
    public Stream<Table> visit(AliasLop aliasNode) {
        final AliasOperator operator = new AliasOperator(aliasNode.schema());

        return apply(aliasNode.input()).map(operator::compute);
    }

    @Override
    public Stream<Table> visit(BreakdownLop breakdownNode) {
        final BreakdownOperator operator = new BreakdownOperator(breakdownNode.breakdowns());

        return apply(breakdownNode.input()).flatMap(operator::compute);
    }

    @Override
    public Stream<Table> visit(BaseTableLop baseTable) {
        return Stream.of(tables.get(baseTable.name()));
    }

    @Override
    public Stream<Table> visit(DistinctLop distinctNode) {
        final DistinctOperator operator = new DistinctOperator();

        return apply(distinctNode.input()).map(operator::compute);
    }

    @Override
    public Stream<Table> visit(MapLop mapNode) {
        final MapOperator operator = new MapOperator(mapNode.operations(), mapNode.schema());

        return apply(mapNode.input()).map(operator::compute);
    }

    @Override
    public Stream<Table> visit(ProjectionLop projectionNode) {
        final ProjectionOperator operator = new ProjectionOperator(projectionNode.schema());

        return apply(projectionNode.input()).map(operator::compute);
    }
}
