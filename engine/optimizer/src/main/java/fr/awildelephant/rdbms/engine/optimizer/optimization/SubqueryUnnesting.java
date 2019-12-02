package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.DefaultLopVisitor;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.SubqueryExecutionLop;
import fr.awildelephant.rdbms.schema.Schema;

import static fr.awildelephant.rdbms.engine.optimizer.optimization.CorrelatedSubqueryMatcher.isSubqueryCorrelated;
import static java.util.stream.Collectors.toList;

public class SubqueryUnnesting extends DefaultLopVisitor<LogicalOperator> {

    @Override
    public LogicalOperator visit(SubqueryExecutionLop subqueryExecutionLop) {
        final LogicalOperator subquery = subqueryExecutionLop.subquery();

        if (isSubqueryCorrelated(subquery)) {
            return subqueryExecutionLop.transformInputs(this);
        }

        final LogicalOperator transformedInput = apply(subqueryExecutionLop.input());
        final LogicalOperator transformedSubquery = apply(subqueryExecutionLop.subquery());

        final Schema leftSchema = transformedInput.schema();
        final Schema rightSchema = transformedSubquery.schema();

        return new CartesianProductLop(transformedInput,
                                       transformedSubquery,
                                       leftSchema.extend(rightSchema.columnNames().stream()
                                                                    .map(rightSchema::column)
                                                                    .collect(toList())));
    }

    @Override
    public LogicalOperator defaultVisit(LogicalOperator operator) {
        return operator.transformInputs(this);
    }
}
