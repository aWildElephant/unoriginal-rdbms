package fr.awildelephant.rdbms.execution.plan;

import fr.awildelephant.rdbms.execution.operator.AggregationOperator;
import fr.awildelephant.rdbms.execution.operator.AliasOperator;
import fr.awildelephant.rdbms.execution.operator.BaseTableOperator;
import fr.awildelephant.rdbms.execution.operator.CartesianProductOperator;
import fr.awildelephant.rdbms.execution.operator.FilterOperator;
import fr.awildelephant.rdbms.execution.operator.InnerJoinOperator;
import fr.awildelephant.rdbms.execution.operator.LeftJoinOperator;
import fr.awildelephant.rdbms.execution.operator.LimitOperator;
import fr.awildelephant.rdbms.execution.operator.MapOperator;
import fr.awildelephant.rdbms.execution.operator.ProjectionOperator;
import fr.awildelephant.rdbms.execution.operator.SemiJoinOperator;
import fr.awildelephant.rdbms.execution.operator.SortOperator;
import fr.awildelephant.rdbms.execution.operator.TableConstructorOperator;
import fr.awildelephant.rdbms.execution.operator.csv.ReadCSVOperator;
import fr.awildelephant.rdbms.operator.logical.AggregationLop;
import fr.awildelephant.rdbms.operator.logical.AliasLop;
import fr.awildelephant.rdbms.operator.logical.BaseTableLop;
import fr.awildelephant.rdbms.operator.logical.CartesianProductLop;
import fr.awildelephant.rdbms.operator.logical.DefaultLopVisitor;
import fr.awildelephant.rdbms.operator.logical.DistinctLop;
import fr.awildelephant.rdbms.operator.logical.FilterLop;
import fr.awildelephant.rdbms.operator.logical.InnerJoinLop;
import fr.awildelephant.rdbms.operator.logical.LeftJoinLop;
import fr.awildelephant.rdbms.operator.logical.LimitLop;
import fr.awildelephant.rdbms.operator.logical.LogicalOperator;
import fr.awildelephant.rdbms.operator.logical.MapLop;
import fr.awildelephant.rdbms.operator.logical.ProjectionLop;
import fr.awildelephant.rdbms.operator.logical.ReadCSVLop;
import fr.awildelephant.rdbms.operator.logical.ScalarSubqueryLop;
import fr.awildelephant.rdbms.operator.logical.SemiJoinLop;
import fr.awildelephant.rdbms.operator.logical.SortLop;
import fr.awildelephant.rdbms.operator.logical.TableConstructorLop;
import org.apache.commons.lang3.builder.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static fr.awildelephant.rdbms.formula.creation.ValueExpressionToFormulaTransformer.createFormula;

// TODO: if a node appears twice in the LogicalOperator tree, ensure we only compute the PlanStep once
public final class BasicPlanGenerator extends DefaultLopVisitor<String> implements Builder<Plan> {

    private final LogicalOperator logicalTree;
    private final List<PlanStep> steps;

    private int count;

    public BasicPlanGenerator(LogicalOperator logicalTree) {
        this.logicalTree = logicalTree;
        this.steps = new ArrayList<>();
    }

    @Override
    public Plan build() {
        final String targetKey = apply(logicalTree);

        return new Plan(steps, targetKey);
    }

    @Override
    public String visit(AggregationLop aggregation) {
        final String inputKey = apply(aggregation.input());
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(inputKey), new AggregationOperator(inputKey, aggregation.aggregates(), aggregation.breakdowns(), aggregation.schema())));

        return key;
    }

    @Override
    public String visit(AliasLop alias) {
        final String inputKey = apply(alias.input());
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(inputKey), new AliasOperator(inputKey, alias.schema())));

        return key;
    }

    @Override
    public String visit(BaseTableLop baseTable) {
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(), new BaseTableOperator(baseTable.name(), baseTable.version())));

        return key;
    }

    @Override
    public String visit(CartesianProductLop cartesianProduct) {
        final String leftInputKey = apply(cartesianProduct.left());
        final String rightInputKey = apply(cartesianProduct.right());
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(leftInputKey, rightInputKey), new CartesianProductOperator(leftInputKey, rightInputKey, cartesianProduct.schema())));

        return key;
    }

    @Override
    public String visit(DistinctLop distinct) {
        final String inputKey = apply(distinct.input());
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(inputKey), new AggregationOperator(inputKey, List.of(), distinct.schema().columnNames(), distinct.schema())));

        return key;
    }

    @Override
    public String visit(FilterLop filter) {
        final String inputKey = apply(filter.input());
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(inputKey), new FilterOperator(inputKey, createFormula(filter.filter(), filter.schema()))));

        return key;
    }

    @Override
    public String visit(InnerJoinLop innerJoin) {
        final String leftInputKey = apply(innerJoin.left());
        final String rightInputKey = apply(innerJoin.right());
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(leftInputKey, rightInputKey), new InnerJoinOperator(leftInputKey, rightInputKey, innerJoin.schema(), innerJoin.joinSpecification())));

        return key;
    }

    @Override
    public String visit(LeftJoinLop leftJoin) {
        final String leftInputKey = apply(leftJoin.left());
        final String rightInputKey = apply(leftJoin.right());
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(leftInputKey, rightInputKey), new LeftJoinOperator(leftInputKey, rightInputKey, leftJoin.schema(), leftJoin.joinSpecification())));

        return key;
    }

    @Override
    public String visit(LimitLop limit) {
        final String inputKey = apply(limit.input());
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(inputKey), new LimitOperator(inputKey, limit.limit())));

        return key;
    }

    @Override
    public String visit(MapLop map) {
        final String inputKey = apply(map.input());
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(inputKey), new MapOperator(inputKey, map.expressions(), map.schema())));

        return key;
    }

    @Override
    public String visit(ProjectionLop projection) {
        final String inputKey = apply(projection.input());
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(inputKey), new ProjectionOperator(inputKey, projection.schema())));

        return key;
    }

    @Override
    public String visit(ReadCSVLop readCSV) {
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(), new ReadCSVOperator(readCSV.filePath(), readCSV.schema())));

        return key;
    }

    @Override
    public String visit(ScalarSubqueryLop scalarSubquery) {
        return apply(scalarSubquery.input());
    }

    @Override
    public String visit(SemiJoinLop semiJoin) {
        final String leftInputKey = apply(semiJoin.left());
        final String rightInputKey = apply(semiJoin.right());
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(leftInputKey, rightInputKey), new SemiJoinOperator(leftInputKey, rightInputKey, semiJoin.predicate(), semiJoin.schema())));

        return key;
    }

    @Override
    public String visit(SortLop sort) {
        final String inputKey = apply(sort.input());
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(inputKey), new SortOperator(inputKey, sort.sortSpecificationList())));

        return key;
    }

    @Override
    public String visit(TableConstructorLop tableConstructor) {
        final String key = generateKey();

        steps.add(new PlanStep(key, Set.of(), new TableConstructorOperator(tableConstructor.matrix(), tableConstructor.schema())));

        return key;
    }

    @Override
    public String defaultVisit(LogicalOperator operator) {
        throw new IllegalStateException("Unable to create step for node " + operator);
    }

    private String generateKey() {
        return "step-" + count++;
    }
}
