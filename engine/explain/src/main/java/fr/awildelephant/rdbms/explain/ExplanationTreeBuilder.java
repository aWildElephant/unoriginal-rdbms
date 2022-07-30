package fr.awildelephant.rdbms.explain;

import fr.awildelephant.rdbms.execution.AggregationLop;
import fr.awildelephant.rdbms.execution.AliasLop;
import fr.awildelephant.rdbms.execution.BaseTableLop;
import fr.awildelephant.rdbms.execution.CartesianProductLop;
import fr.awildelephant.rdbms.execution.DependentJoinLop;
import fr.awildelephant.rdbms.execution.DependentSemiJoinLop;
import fr.awildelephant.rdbms.execution.DistinctLop;
import fr.awildelephant.rdbms.execution.FilterLop;
import fr.awildelephant.rdbms.execution.InnerJoinLop;
import fr.awildelephant.rdbms.execution.LeftJoinLop;
import fr.awildelephant.rdbms.execution.LimitLop;
import fr.awildelephant.rdbms.execution.LopVisitor;
import fr.awildelephant.rdbms.execution.MapLop;
import fr.awildelephant.rdbms.execution.ProjectionLop;
import fr.awildelephant.rdbms.execution.ScalarSubqueryLop;
import fr.awildelephant.rdbms.execution.SemiJoinLop;
import fr.awildelephant.rdbms.execution.SortLop;
import fr.awildelephant.rdbms.execution.TableConstructorLop;
import fr.awildelephant.rdbms.explain.tree.Tree;

import static fr.awildelephant.rdbms.explain.tree.Tree.leaf;
import static fr.awildelephant.rdbms.explain.tree.Tree.node;

public final class ExplanationTreeBuilder implements LopVisitor<Tree<String>> {

    @Override
    public Tree<String> visit(AggregationLop aggregation) {
        return node("Aggregation", apply(aggregation.input()));
    }

    @Override
    public Tree<String> visit(AliasLop alias) {
        return node("Aliasing", apply(alias.input()));
    }

    @Override
    public Tree<String> visit(BaseTableLop baseTable) {
        return leaf("Base table [" + baseTable.name() + ']');
    }

    @Override
    public Tree<String> visit(CartesianProductLop cartesianProduct) {
        return node("Cartesian product", apply(cartesianProduct.left()), apply(cartesianProduct.right()));
    }

    @Override
    public Tree<String> visit(DependentJoinLop dependentJoin) {
        return node("Dependent join", apply(dependentJoin.left()), apply(dependentJoin.right()));
    }

    @Override
    public Tree<String> visit(DependentSemiJoinLop dependentSemiJoin) {
        return node("Dependent semi-join", apply(dependentSemiJoin.left()), apply(dependentSemiJoin.right()));
    }

    @Override
    public Tree<String> visit(DistinctLop distinct) {
        return node("Distinct", apply(distinct.input()));
    }

    @Override
    public Tree<String> visit(FilterLop filter) {
        return node("Filter", apply(filter.input()));
    }

    @Override
    public Tree<String> visit(InnerJoinLop innerJoin) {
        return node("Inner join", apply(innerJoin.left()), apply(innerJoin.right()));
    }

    @Override
    public Tree<String> visit(LeftJoinLop leftJoin) {
        return node("Left join", apply(leftJoin.left()), apply(leftJoin.right()));
    }

    @Override
    public Tree<String> visit(LimitLop limit) {
        return node("Limit", apply(limit.input()));
    }

    @Override
    public Tree<String> visit(MapLop map) {
        return node("Map", apply(map.input()));
    }

    @Override
    public Tree<String> visit(ProjectionLop projection) {
        return node("Projection", apply(projection.input()));
    }

    @Override
    public Tree<String> visit(ScalarSubqueryLop scalarSubquery) {
        return node("Scalar subquery", apply(scalarSubquery.input()));
    }

    @Override
    public Tree<String> visit(SemiJoinLop semiJoin) {
        return node("Semi-join", apply(semiJoin.left()), apply(semiJoin.right()));
    }

    @Override
    public Tree<String> visit(SortLop sort) {
        return node("Sort", apply(sort.input()));
    }

    @Override
    public Tree<String> visit(TableConstructorLop tableConstructor) {
        return leaf("Matrix");
    }
}
