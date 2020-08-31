package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.*;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;

import java.util.List;

import static fr.awildelephant.rdbms.engine.optimizer.optimization.CorrelatedFilterMatcher.isCorrelated;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class CorrelatedSubqueryMatcher implements LopVisitor<Boolean> {

    private static final CorrelatedSubqueryMatcher INSTANCE = new CorrelatedSubqueryMatcher();

    public static boolean isSubqueryCorrelated(LogicalOperator subquery) {
        return TRUE.equals(INSTANCE.apply(subquery));
    }


    @Override
    public Boolean visit(AggregationLop aggregationNode) {
        return apply(aggregationNode.input());
    }

    @Override
    public Boolean visit(AliasLop alias) {
        return apply(alias.input());
    }

    @Override
    public Boolean visit(BaseTableLop baseTable) {
        return FALSE;
    }

    @Override
    public Boolean visit(CartesianProductLop cartesianProductNode) {
        return apply(cartesianProductNode.leftInput()) || apply(cartesianProductNode.rightInput());
    }

    @Override
    public Boolean visit(DistinctLop distinctNode) {
        return apply(distinctNode.input());
    }

    @Override
    public Boolean visit(FilterLop filter) {
        return isCorrelated(filter.filter()) || apply(filter.input());
    }

    @Override
    public Boolean visit(InnerJoinLop innerJoinLop) {
        return isCorrelated(innerJoinLop.joinSpecification())
                || apply(innerJoinLop.left())
                || apply(innerJoinLop.right());
    }

    @Override
    public Boolean visit(LeftJoinLop leftJoinLop) {
        return isCorrelated(leftJoinLop.joinSpecification())
                || apply(leftJoinLop.left())
                || apply(leftJoinLop.right());
    }

    @Override
    public Boolean visit(LimitLop limitLop) {
        return apply(limitLop.input());
    }

    @Override
    public Boolean visit(MapLop mapNode) {
        return mapNode.expressions().stream().anyMatch(CorrelatedFilterMatcher::isCorrelated) || apply(mapNode.input());
    }

    @Override
    public Boolean visit(ProjectionLop projectionNode) {
        return apply(projectionNode.input());
    }

    @Override
    public Boolean visit(ScalarSubqueryLop scalarSubquery) {
        return apply(scalarSubquery.input());
    }

    @Override
    public Boolean visit(SemiJoinLop semiJoin) {
        return isCorrelated(semiJoin.predicate())
                || apply(semiJoin.left())
                || apply(semiJoin.right());
    }

    @Override
    public Boolean visit(SortLop sortLop) {
        return apply(sortLop.input());
    }

    @Override
    public Boolean visit(SubqueryExecutionLop subqueryExecutionLop) {
        return apply(subqueryExecutionLop.input());
    }

    @Override
    public Boolean visit(TableConstructorLop tableConstructor) {
        for (List<ValueExpression> row : tableConstructor.matrix()) {
            for (ValueExpression cell : row) {
                if (isCorrelated(cell)) {
                    return TRUE;
                }
            }
        }

        return FALSE;
    }
}
