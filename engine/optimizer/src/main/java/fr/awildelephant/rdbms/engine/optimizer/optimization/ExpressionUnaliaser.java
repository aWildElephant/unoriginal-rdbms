package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.alias.Alias;
import fr.awildelephant.rdbms.plan.arithmetic.DefaultValueExpressionVisitor;
import fr.awildelephant.rdbms.plan.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.plan.arithmetic.Variable;

import static fr.awildelephant.rdbms.plan.arithmetic.OuterQueryVariable.outerQueryVariable;
import static fr.awildelephant.rdbms.plan.arithmetic.Variable.variable;

public final class ExpressionUnaliaser extends DefaultValueExpressionVisitor<ValueExpression> {

    private final Alias alias;

    ExpressionUnaliaser(Alias alias) {
        this.alias = alias;
    }

    @Override
    public ValueExpression visit(OuterQueryVariable outerQueryVariable) {
        return outerQueryVariable(alias.unalias(outerQueryVariable.reference()), outerQueryVariable.domain());
    }

    @Override
    public ValueExpression visit(Variable variable) {
        return variable(alias.unalias(variable.reference()), variable.domain());
    }

    @Override
    protected ValueExpression defaultVisit(ValueExpression expression) {
        return expression.transformInputs(this);
    }
}
