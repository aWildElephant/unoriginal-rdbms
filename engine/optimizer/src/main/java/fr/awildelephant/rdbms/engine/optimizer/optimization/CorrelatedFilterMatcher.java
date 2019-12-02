package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.arithmetic.AddExpression;
import fr.awildelephant.rdbms.plan.arithmetic.AndExpression;
import fr.awildelephant.rdbms.plan.arithmetic.BetweenExpression;
import fr.awildelephant.rdbms.plan.arithmetic.CaseWhenExpression;
import fr.awildelephant.rdbms.plan.arithmetic.CastExpression;
import fr.awildelephant.rdbms.plan.arithmetic.DefaultValueExpressionVisitor;
import fr.awildelephant.rdbms.plan.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public final class CorrelatedFilterMatcher extends DefaultValueExpressionVisitor<Boolean> {

    private static final CorrelatedFilterMatcher INSTANCE = new CorrelatedFilterMatcher();

    public static boolean isCorrelated(ValueExpression expression) {
        return TRUE.equals(INSTANCE.apply(expression));
    }

    @Override
    public Boolean visit(AddExpression add) {
        return apply(add.left()) || apply(add.right());
    }

    @Override
    public Boolean visit(AndExpression and) {
        return apply(and.left()) || apply(and.right());
    }

    @Override
    public Boolean visit(BetweenExpression between) {
        return apply(between.value()) || apply(between.lowerBound()) || apply(between.upperBound());
    }

    @Override
    public Boolean visit(CaseWhenExpression caseWhen) {
        return apply(caseWhen.condition()) || apply(caseWhen.thenExpression()) || apply(caseWhen.elseExpression());
    }

    @Override
    public Boolean visit(CastExpression cast) {
        return apply(cast.input());
    }

    @Override
    public Boolean visit(EqualExpression equal) {
        return apply(equal.left()) || apply(equal.right());
    }

    @Override
    public Boolean visit(OuterQueryVariable outerQueryVariable) {
        return TRUE;
    }


    @Override
    protected Boolean defaultVisit(ValueExpression expression) {
        return FALSE;
    }
}
