package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.DefaultLopVisitor;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.plan.arithmetic.FilterExpander.expandFilters;

public final class JoinSplitter extends DefaultLopVisitor<MultiJoin> {

    @Override
    public MultiJoin visit(CartesianProductLop cartesianProduct) {
        final MultiJoin left = apply(cartesianProduct.left());
        final MultiJoin right = apply(cartesianProduct.right());

        final ArrayList<LogicalOperator> operators = new ArrayList<>(left.operators());
        operators.addAll(right.operators());
        final List<ValueExpression> expressions = new ArrayList<>(left.expressions());
        expressions.addAll(right.expressions());

        return new MultiJoin(operators, expressions);
    }

    @Override
    public MultiJoin visit(InnerJoinLop innerJoin) {
        final MultiJoin left = apply(innerJoin.left());
        final MultiJoin right = apply(innerJoin.right());

        final ArrayList<LogicalOperator> operators = new ArrayList<>(left.operators());
        operators.addAll(right.operators());
        final List<ValueExpression> expressions = new ArrayList<>(left.expressions());
        expressions.addAll(right.expressions());
        expressions.addAll(expandFilters(innerJoin.joinSpecification()));

        return new MultiJoin(operators, expressions);
    }

    @Override
    public MultiJoin defaultVisit(LogicalOperator operator) {
        return new MultiJoin(List.of(operator), List.of());
    }
}
