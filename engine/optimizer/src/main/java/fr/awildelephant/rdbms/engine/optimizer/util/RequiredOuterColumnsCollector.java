package fr.awildelephant.rdbms.engine.optimizer.util;

import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.DependentJoinLop;
import fr.awildelephant.rdbms.plan.DependentSemiJoinLop;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LeftJoinLop;
import fr.awildelephant.rdbms.plan.LimitLop;
import fr.awildelephant.rdbms.plan.LopVisitor;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.ScalarSubqueryLop;
import fr.awildelephant.rdbms.plan.SemiJoinLop;
import fr.awildelephant.rdbms.plan.SortLop;
import fr.awildelephant.rdbms.plan.TableConstructorLop;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Collection;
import java.util.stream.Stream;

final class RequiredOuterColumnsCollector implements LopVisitor<Stream<ColumnReference>> {

    private final OuterQueryVariableExtractor outerQueryVariableExtractor = new OuterQueryVariableExtractor();

    @Override
    public Stream<ColumnReference> visit(AggregationLop aggregationNode) {
        return apply(aggregationNode.input());
    }

    @Override
    public Stream<ColumnReference> visit(AliasLop alias) {
        return apply(alias.input());
    }

    @Override
    public Stream<ColumnReference> visit(BaseTableLop baseTable) {
        return Stream.of();
    }

    @Override
    public Stream<ColumnReference> visit(CartesianProductLop cartesianProductNode) {
        return Stream.concat(apply(cartesianProductNode.leftInput()), apply(cartesianProductNode.rightInput()));
    }

    @Override
    public Stream<ColumnReference> visit(DistinctLop distinctNode) {
        return apply(distinctNode.input());
    }

    @Override
    public Stream<ColumnReference> visit(FilterLop filter) {
        return Stream.concat(apply(filter.input()), outerQueryVariableExtractor.apply(filter.filter()));
    }

    @Override
    public Stream<ColumnReference> visit(InnerJoinLop innerJoinLop) {
        return StreamHelper.concat(apply(innerJoinLop.left()),
                                   apply(innerJoinLop.right()),
                                   outerQueryVariableExtractor.apply(innerJoinLop.joinSpecification()));
    }

    @Override
    public Stream<ColumnReference> visit(LeftJoinLop leftJoinLop) {
        return StreamHelper.concat(apply(leftJoinLop.left()),
                                   apply(leftJoinLop.right()),
                                   outerQueryVariableExtractor.apply(leftJoinLop.joinSpecification()));
    }

    @Override
    public Stream<ColumnReference> visit(LimitLop limitLop) {
        return apply(limitLop.input());
    }

    @Override
    public Stream<ColumnReference> visit(MapLop mapNode) {
        return apply(mapNode.input());
    }

    @Override
    public Stream<ColumnReference> visit(ProjectionLop projectionNode) {
        return apply(projectionNode.input());
    }

    @Override
    public Stream<ColumnReference> visit(ScalarSubqueryLop scalarSubquery) {
        return apply(scalarSubquery.input());
    }

    @Override
    public Stream<ColumnReference> visit(SemiJoinLop semiJoin) {
        return StreamHelper.concat(apply(semiJoin.left()),
                                   apply(semiJoin.right()),
                                   outerQueryVariableExtractor.apply(semiJoin.predicate()));
    }

    @Override
    public Stream<ColumnReference> visit(SortLop sortLop) {
        return apply(sortLop.input());
    }

    @Override
    public Stream<ColumnReference> visit(DependentJoinLop dependentJoin) {
        return StreamHelper.concat(apply(dependentJoin.left()),
                                   apply(dependentJoin.right()),
                                   outerQueryVariableExtractor.apply(dependentJoin.predicate()));
    }

    @Override
    public Stream<ColumnReference> visit(DependentSemiJoinLop dependentSemiJoin) {
        return StreamHelper.concat(apply(dependentSemiJoin.left()),
                                   apply(dependentSemiJoin.right()),
                                   outerQueryVariableExtractor.apply(dependentSemiJoin.predicate()));
    }

    @Override
    public Stream<ColumnReference> visit(TableConstructorLop tableConstructor) {
        return tableConstructor.matrix().stream().flatMap(Collection::stream).flatMap(outerQueryVariableExtractor);
    }
}
