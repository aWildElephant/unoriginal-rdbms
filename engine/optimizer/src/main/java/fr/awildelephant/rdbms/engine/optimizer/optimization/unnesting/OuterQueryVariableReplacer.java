package fr.awildelephant.rdbms.engine.optimizer.optimization.unnesting;

import fr.awildelephant.rdbms.execution.arithmetic.DefaultValueExpressionVisitor;
import fr.awildelephant.rdbms.execution.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.execution.arithmetic.ValueExpression;

import static fr.awildelephant.rdbms.execution.arithmetic.Variable.variable;

public final class OuterQueryVariableReplacer extends DefaultValueExpressionVisitor<ValueExpression> {

    @Override
    public ValueExpression visit(OuterQueryVariable outerQueryVariable) {
        return variable(outerQueryVariable.reference(), outerQueryVariable.domain());
    }

    @Override
    protected ValueExpression defaultVisit(ValueExpression expression) {
        return expression.transformInputs(this);
    }
}
