package fr.awildelephant.rdbms.engine.operators.subquery;

import fr.awildelephant.rdbms.engine.data.record.Tuple;
import fr.awildelephant.rdbms.plan.DefaultLopVisitor;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.TableConstructorLop;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public final class LogicalOperatorOuterQueryReferenceReplacer extends DefaultLopVisitor<LogicalOperator> {

    private final ValueExpressionOuterQueryReferenceReplacer valueExpressionReplacer;

    public LogicalOperatorOuterQueryReferenceReplacer(Schema schema, Tuple record) {
        valueExpressionReplacer = new ValueExpressionOuterQueryReferenceReplacer(schema, record);
    }

    @Override
    public LogicalOperator visit(FilterLop filter) {
        return new FilterLop(apply(filter.input()), valueExpressionReplacer.apply(filter.filter()));
    }

    @Override
    public LogicalOperator visit(InnerJoinLop innerJoin) {
        return new InnerJoinLop(apply(innerJoin.left()),
                                apply(innerJoin.right()),
                                valueExpressionReplacer.apply(innerJoin.joinSpecification()),
                                innerJoin.schema());
    }

    @Override
    public LogicalOperator visit(MapLop map) {
        final List<ValueExpression> expressions = map.expressions();

        final List<ValueExpression> transformedExpressions = new ArrayList<>(expressions.size());
        for (ValueExpression expression : expressions) {
            transformedExpressions.add(valueExpressionReplacer.apply(expression));
        }

        return new MapLop(apply(map.input()), transformedExpressions, map.expressionsOutputNames());
    }

    @Override
    public LogicalOperator visit(TableConstructorLop tableConstructor) {
        final List<List<ValueExpression>> matrix = tableConstructor.matrix();
        final List<List<ValueExpression>> transformedMatrix = new ArrayList<>(matrix.size());
        for (List<ValueExpression> row : matrix) {
            final List<ValueExpression> transformedRow = new ArrayList<>(row.size());

            for (ValueExpression expression : row) {
                transformedRow.add(valueExpressionReplacer.apply(expression));
            }

            transformedMatrix.add(transformedRow);
        }

        return new TableConstructorLop(transformedMatrix);
    }

    @Override
    public LogicalOperator defaultVisit(LogicalOperator operator) {
        return operator.transformInputs(this);
    }
}
