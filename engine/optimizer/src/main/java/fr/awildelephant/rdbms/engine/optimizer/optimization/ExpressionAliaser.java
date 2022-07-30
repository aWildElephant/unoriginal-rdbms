package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.execution.alias.Alias;
import fr.awildelephant.rdbms.execution.arithmetic.DefaultValueExpressionVisitor;
import fr.awildelephant.rdbms.execution.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.execution.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.execution.arithmetic.Variable;

import static fr.awildelephant.rdbms.execution.arithmetic.OuterQueryVariable.outerQueryVariable;
import static fr.awildelephant.rdbms.execution.arithmetic.Variable.variable;

public class ExpressionAliaser extends DefaultValueExpressionVisitor<ValueExpression> {

    private final Alias alias;

    public ExpressionAliaser(Alias alias) {
        this.alias = alias;
    }

    @Override
    public ValueExpression visit(OuterQueryVariable outerQueryVariable) {
        return outerQueryVariable(alias.alias(outerQueryVariable.reference()), outerQueryVariable.domain());
    }

    @Override
    public ValueExpression visit(Variable variable) {
        return variable(alias.alias(variable.reference()), variable.domain());
    }

    @Override
    protected ValueExpression defaultVisit(ValueExpression expression) {
        return expression.transformInputs(this);
    }
}
