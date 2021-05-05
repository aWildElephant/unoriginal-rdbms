package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.DefaultLopVisitor;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.MapLop;

import java.util.stream.Collectors;

public class SimplifyExpressions extends DefaultLopVisitor<LogicalOperator> {

    private final ExpressionSimplifier expressionSimplifier = new ExpressionSimplifier();

    @Override
    public LogicalOperator visit(FilterLop filter) {
        return new FilterLop(apply(filter.input()), expressionSimplifier.apply(filter.filter()));
    }

    @Override
    public LogicalOperator visit(MapLop mapNode) {
        return new MapLop(apply(mapNode.input()),
                          mapNode.expressions().stream().map(expressionSimplifier).collect(Collectors.toList()),
                          mapNode.expressionsOutputNames());
    }

    @Override
    public LogicalOperator defaultVisit(LogicalOperator operator) {
        return operator.transformInputs(this);
    }
}
