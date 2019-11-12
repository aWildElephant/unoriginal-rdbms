package fr.awildelephant.rdbms.plan.arithmetic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static fr.awildelephant.rdbms.plan.arithmetic.FilterCollapser.collapseFilters;
import static fr.awildelephant.rdbms.plan.arithmetic.FilterExpander.expandFilters;
import static fr.awildelephant.rdbms.plan.arithmetic.OrExpression.orExpression;

public final class OrExpressionFactorizer extends DefaultValueExpressionVisitor<ValueExpression> {

    private static final OrExpressionFactorizer INSTANCE = new OrExpressionFactorizer();

    public static ValueExpression factorizeOrExpression(ValueExpression input) {
        return INSTANCE.apply(input);
    }

    @Override
    public ValueExpression visit(OrExpression or) {
        final ValueExpression left = apply(or.left());
        final ValueExpression right = apply(or.right());

        final List<ValueExpression> leftExpressions = expandFilters(left);
        final List<ValueExpression> rightExpressions = expandFilters(right);

        final List<ValueExpression> commonExpressions = new ArrayList<>();
        final List<ValueExpression> leftOnlyExpressions = new ArrayList<>();
        final Set<ValueExpression> rightOnlyExpressions = new HashSet<>(rightExpressions);

        for (ValueExpression expression : leftExpressions) {
            if (rightOnlyExpressions.remove(expression)) {
                commonExpressions.add(expression);
            } else {
                leftOnlyExpressions.add(expression);
            }
        }

        if (leftOnlyExpressions.isEmpty()) {
            return angryCollapseFilters(rightExpressions);
        } else if (rightOnlyExpressions.isEmpty()) {
            return angryCollapseFilters(leftExpressions);
        } else {
            commonExpressions.add(orExpression(angryCollapseFilters(leftOnlyExpressions),
                                               angryCollapseFilters(rightOnlyExpressions)));

            return angryCollapseFilters(commonExpressions);
        }
    }

    private ValueExpression angryCollapseFilters(Iterable<ValueExpression> rightExpressions) {
        return collapseFilters(rightExpressions).orElseThrow(IllegalStateException::new);
    }

    @Override
    protected ValueExpression defaultVisit(ValueExpression expression) {
        return expression;
    }
}
