package fr.awildelephant.rdbms.engine.optimizer.util;

import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class FreeVariablesFunction implements Function<LogicalOperator, Set<ColumnReference>> {

    private final RequiredOuterColumnsCollector requiredOuterColumnsCollector = new RequiredOuterColumnsCollector();

    @Override
    public Set<ColumnReference> apply(LogicalOperator logicalOperator) {
        return requiredOuterColumnsCollector.apply(logicalOperator).collect(Collectors.toSet());
    }
}
