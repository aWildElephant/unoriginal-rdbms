package fr.awildelephant.rdbms.engine.optimizer;

import fr.awildelephant.rdbms.plan.DefaultLopVisitor;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.alias.ExactMatchAlias;

import java.util.stream.Collectors;

public final class FreeVariableAliasing extends DefaultLopVisitor<LogicalOperator> {

    private final OuterQueryVariableAliaser outerQueryVariableAliaser;

    public FreeVariableAliasing(ExactMatchAlias alias) {
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
