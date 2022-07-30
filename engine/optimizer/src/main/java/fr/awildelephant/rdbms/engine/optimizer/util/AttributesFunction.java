package fr.awildelephant.rdbms.engine.optimizer.util;

import fr.awildelephant.rdbms.execution.LogicalOperator;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public final class AttributesFunction implements Function<LogicalOperator, Set<ColumnReference>> {

    private static final AttributesFunction INSTANCE = new AttributesFunction();

    public static Set<ColumnReference> attributes(LogicalOperator operator) {
        return INSTANCE.apply(operator);
    }

    @Override
    public Set<ColumnReference> apply(LogicalOperator operator) {
        return new HashSet<>(operator.schema().columnNames());
    }
}
