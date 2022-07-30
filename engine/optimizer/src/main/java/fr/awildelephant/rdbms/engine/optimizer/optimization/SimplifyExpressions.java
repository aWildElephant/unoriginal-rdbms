package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.execution.DefaultLopVisitor;
import fr.awildelephant.rdbms.execution.FilterLop;
import fr.awildelephant.rdbms.execution.LogicalOperator;
import fr.awildelephant.rdbms.execution.MapLop;

import java.util.stream.Collectors;

public class SimplifyExpressions extends DefaultLopVisitor<LogicalOperator> {

    private final ExpressionSimplifier expressionSimplifier = new ExpressionSimplifier();

    @Override
    public LogicalOperator visit(FilterLop filter) {
        return new FilterLop(apply(filter.input()), expressionSimplifier.apply(filter.filter()));
    }

    @Override
    public LogicalOperator visit(MapLop map) {
        return new MapLop(apply(map.input()),
                          map.expressions().stream().map(expressionSimplifier).collect(Collectors.toList()),
                          map.expressionsOutputNames());
    }

    @Override
    public LogicalOperator defaultVisit(LogicalOperator operator) {
        return operator.transformInputs(this);
    }
}
