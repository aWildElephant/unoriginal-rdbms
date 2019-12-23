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
    public MultiJoin visit(CartesianProductLop cartesianProductNode) {
        final MultiJoin left = apply(cartesianProductNode.leftInput());
        final MultiJoin right = apply(cartesianProductNode.rightInput());

        final ArrayList<LogicalOperator> operators = new ArrayList<>(left.operators());
        operators.addAll(right.operators());
        final List<ValueExpression> expressions = new ArrayList<>(left.expressions());
        expressions.addAll(right.expressions());

        return new MultiJoin(operators, expressions);
    }

    @Override
    public MultiJoin visit(InnerJoinLop innerJoinLop) {
        final MultiJoin left = apply(innerJoinLop.left());
        final MultiJoin right = apply(innerJoinLop.right());

        final ArrayList<LogicalOperator> operators = new ArrayList<>(left.operators());
        operators.addAll(right.operators());
        final List<ValueExpression> expressions = new ArrayList<>(left.expressions());
        expressions.addAll(right.expressions());
        expressions.addAll(expandFilters(innerJoinLop.joinSpecification()));

        return new MultiJoin(operators, expressions);
    }

    @Override
    public MultiJoin defaultVisit(LogicalOperator operator) {
        return new MultiJoin(List.of(operator), List.of());
    }
}
