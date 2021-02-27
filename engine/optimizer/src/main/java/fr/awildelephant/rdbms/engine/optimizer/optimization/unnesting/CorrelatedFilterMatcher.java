package fr.awildelephant.rdbms.engine.optimizer.optimization.unnesting;

import fr.awildelephant.rdbms.plan.arithmetic.ConstantExpression;
import fr.awildelephant.rdbms.plan.arithmetic.DefaultValueExpressionVisitor;
import fr.awildelephant.rdbms.plan.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.plan.arithmetic.Variable;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

final class CorrelatedFilterMatcher extends DefaultValueExpressionVisitor<Boolean> {

    private static final CorrelatedFilterMatcher INSTANCE = new CorrelatedFilterMatcher();

    static boolean isCorrelated(ValueExpression expression) {
        return TRUE.equals(INSTANCE.apply(expression));
    }

    @Override
    public Boolean visit(ConstantExpression constant) {
        return FALSE;
    }

    @Override
    public Boolean visit(Variable variable) {
        return FALSE;
    }

    @Override
    public Boolean visit(OuterQueryVariable outerQueryVariable) {
        return TRUE;
    }

    @Override
    protected Boolean defaultVisit(ValueExpression expression) {
        return expression.reduce(this, (a, b) -> TRUE.equals(a) || TRUE.equals(b));
    }
}
