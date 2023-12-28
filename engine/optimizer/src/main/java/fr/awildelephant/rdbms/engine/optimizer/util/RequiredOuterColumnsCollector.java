package fr.awildelephant.rdbms.engine.optimizer.util;

import fr.awildelephant.rdbms.operator.logical.AggregationLop;
import fr.awildelephant.rdbms.operator.logical.AliasLop;
import fr.awildelephant.rdbms.operator.logical.BaseTableLop;
import fr.awildelephant.rdbms.operator.logical.CartesianProductLop;
import fr.awildelephant.rdbms.operator.logical.DependentJoinLop;
import fr.awildelephant.rdbms.operator.logical.DependentSemiJoinLop;
import fr.awildelephant.rdbms.operator.logical.DistinctLop;
import fr.awildelephant.rdbms.operator.logical.FilterLop;
import fr.awildelephant.rdbms.operator.logical.InnerJoinLop;
import fr.awildelephant.rdbms.operator.logical.LeftJoinLop;
import fr.awildelephant.rdbms.operator.logical.LimitLop;
import fr.awildelephant.rdbms.operator.logical.LopVisitor;
import fr.awildelephant.rdbms.operator.logical.MapLop;
import fr.awildelephant.rdbms.operator.logical.ProjectionLop;
import fr.awildelephant.rdbms.operator.logical.ReadCSVLop;
import fr.awildelephant.rdbms.operator.logical.ScalarSubqueryLop;
import fr.awildelephant.rdbms.operator.logical.SemiJoinLop;
import fr.awildelephant.rdbms.operator.logical.SortLop;
import fr.awildelephant.rdbms.operator.logical.TableConstructorLop;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Collection;
import java.util.stream.Stream;

final class RequiredOuterColumnsCollector implements LopVisitor<Stream<ColumnReference>> {

    private final OuterQueryVariableExtractor outerQueryVariableExtractor = new OuterQueryVariableExtractor();

    @Override
    public Stream<ColumnReference> visit(AggregationLop aggregation) {
        return apply(aggregation.input());
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
    public Stream<ColumnReference> visit(CartesianProductLop cartesianProduct) {
        return Stream.concat(apply(cartesianProduct.left()), apply(cartesianProduct.right()));
    }

    @Override
    public Stream<ColumnReference> visit(DistinctLop distinct) {
        return apply(distinct.input());
    }

    @Override
    public Stream<ColumnReference> visit(FilterLop filter) {
        return Stream.concat(apply(filter.input()), outerQueryVariableExtractor.apply(filter.filter()));
    }

    @Override
    public Stream<ColumnReference> visit(InnerJoinLop innerJoin) {
        return StreamHelper.concat(apply(innerJoin.left()),
                                   apply(innerJoin.right()),
                                   outerQueryVariableExtractor.apply(innerJoin.joinSpecification()));
    }

    @Override
    public Stream<ColumnReference> visit(LeftJoinLop leftJoin) {
        return StreamHelper.concat(apply(leftJoin.left()),
                                   apply(leftJoin.right()),
                                   outerQueryVariableExtractor.apply(leftJoin.joinSpecification()));
    }

    @Override
    public Stream<ColumnReference> visit(LimitLop limit) {
        return apply(limit.input());
    }

    @Override
    public Stream<ColumnReference> visit(MapLop map) {
        return apply(map.input());
    }

    @Override
    public Stream<ColumnReference> visit(ProjectionLop projection) {
        return apply(projection.input());
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
    public Stream<ColumnReference> visit(SortLop sort) {
        return apply(sort.input());
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
    public Stream<ColumnReference> visit(ReadCSVLop readCSV) {
        return Stream.of();
    }

    @Override
    public Stream<ColumnReference> visit(TableConstructorLop tableConstructor) {
        return tableConstructor.matrix().stream().flatMap(Collection::stream).flatMap(outerQueryVariableExtractor);
    }
}
