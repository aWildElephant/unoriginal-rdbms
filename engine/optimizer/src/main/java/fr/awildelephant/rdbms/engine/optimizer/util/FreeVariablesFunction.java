package fr.awildelephant.rdbms.engine.optimizer.util;

import fr.awildelephant.rdbms.execution.LogicalOperator;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class FreeVariablesFunction implements Function<LogicalOperator, Set<ColumnReference>> {

    private static final FreeVariablesFunction INSTANCE = new FreeVariablesFunction();

    private final RequiredOuterColumnsCollector requiredOuterColumnsCollector = new RequiredOuterColumnsCollector();

    public static Set<ColumnReference> freeVariables(LogicalOperator operator) {
        return INSTANCE.apply(operator);
    }

    @Override
    public Set<ColumnReference> apply(LogicalOperator logicalOperator) {
        return requiredOuterColumnsCollector.apply(logicalOperator).collect(Collectors.toSet());
    }
}
