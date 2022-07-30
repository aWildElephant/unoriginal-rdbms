package fr.awildelephant.rdbms.engine.optimizer;

import fr.awildelephant.rdbms.execution.DefaultLopVisitor;
import fr.awildelephant.rdbms.execution.FilterLop;
import fr.awildelephant.rdbms.execution.LogicalOperator;
import fr.awildelephant.rdbms.execution.MapLop;
import fr.awildelephant.rdbms.execution.alias.Alias;

import java.util.stream.Collectors;

public final class FreeVariableAliasing extends DefaultLopVisitor<LogicalOperator> {

    private final OuterQueryVariableAliaser outerQueryVariableAliaser;

    public FreeVariableAliasing(Alias alias) {
        outerQueryVariableAliaser = new OuterQueryVariableAliaser(alias);
    }

    @Override
    public LogicalOperator visit(FilterLop filter) {
        return new FilterLop(apply(filter.input()), outerQueryVariableAliaser.apply(filter.filter()));
    }

    @Override
    public LogicalOperator visit(MapLop map) {
        return new MapLop(apply(map.input()),
                          map.expressions().stream().map(outerQueryVariableAliaser).collect(Collectors.toList()),
                          map.expressionsOutputNames());
    }

    @Override
    public LogicalOperator defaultVisit(LogicalOperator operator) {
        return operator.transformInputs(this);
    }
}
