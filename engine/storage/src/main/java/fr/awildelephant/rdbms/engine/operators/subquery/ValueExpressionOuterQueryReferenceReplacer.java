package fr.awildelephant.rdbms.engine.operators.subquery;

import fr.awildelephant.rdbms.engine.data.record.Tuple;
import fr.awildelephant.rdbms.plan.arithmetic.DefaultValueExpressionVisitor;
import fr.awildelephant.rdbms.plan.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.Schema;

import static fr.awildelephant.rdbms.plan.arithmetic.ConstantExpression.constantExpression;

final class ValueExpressionOuterQueryReferenceReplacer extends DefaultValueExpressionVisitor<ValueExpression> {

    private final Schema schema;
    private final Tuple record;

    ValueExpressionOuterQueryReferenceReplacer(Schema schema, Tuple record) {
        this.schema = schema;
        this.record = record;
    }

    @Override
    public ValueExpression visit(OuterQueryVariable outerQueryVariable) {
        final ColumnMetadata column = schema.column(outerQueryVariable.reference());

        return constantExpression(record.get(column.index()), column.domain());
    }

    @Override
    protected ValueExpression defaultVisit(ValueExpression expression) {
        return expression.transformInputs(this);
    }
}
