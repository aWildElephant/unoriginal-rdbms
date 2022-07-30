package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.execution.arithmetic.DefaultValueExpressionVisitor;
import fr.awildelephant.rdbms.execution.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.execution.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.execution.arithmetic.Variable;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Map;

import static fr.awildelephant.rdbms.execution.arithmetic.ConstantExpression.constantExpression;

public final class VariableTransformer extends DefaultValueExpressionVisitor<ValueExpression> {

    private final Map<ColumnReference, ? extends DomainValue> values;

    public VariableTransformer(Map<ColumnReference, ? extends DomainValue> values) {
        this.values = values;
    }

    @Override
    public ValueExpression visit(OuterQueryVariable outerQueryVariable) {
        final DomainValue value = values.get(outerQueryVariable.reference());

        if (value == null) {
            return outerQueryVariable;
        }

        return constantExpression(value, outerQueryVariable.domain());
    }

    @Override
    public ValueExpression visit(Variable variable) {
        final DomainValue value = values.get(variable.reference());

        if (value == null) {
            return variable;
        }

        return constantExpression(value, variable.domain());
    }

    @Override
    public ValueExpression defaultVisit(ValueExpression expression) {
        return expression.transformInputs(this);
    }
}
