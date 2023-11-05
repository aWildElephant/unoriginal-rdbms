package fr.awildelephant.rdbms.engine.optimizer;

import fr.awildelephant.rdbms.arithmetic.DefaultValueExpressionVisitor;
import fr.awildelephant.rdbms.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.operator.logical.alias.Alias;

import static fr.awildelephant.rdbms.arithmetic.OuterQueryVariable.outerQueryVariable;

public final class OuterQueryVariableAliaser extends DefaultValueExpressionVisitor<ValueExpression> {

    private final Alias alias;

    public OuterQueryVariableAliaser(Alias alias) {
        this.alias = alias;
    }

    @Override
    public ValueExpression visit(OuterQueryVariable outerQueryVariable) {
        return outerQueryVariable(alias.alias(outerQueryVariable.reference()), outerQueryVariable.domain());
    }

    @Override
    protected ValueExpression defaultVisit(ValueExpression expression) {
        return expression.transformInputs(this);
    }
}
