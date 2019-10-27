package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.ManagedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.AggregationOperator;
import fr.awildelephant.rdbms.engine.operators.AliasOperator;
import fr.awildelephant.rdbms.engine.operators.BreakdownOperator;
import fr.awildelephant.rdbms.engine.operators.CartesianProductOperator;
import fr.awildelephant.rdbms.engine.operators.DistinctOperator;
import fr.awildelephant.rdbms.engine.operators.FilterOperator;
import fr.awildelephant.rdbms.engine.operators.InnerHashJoinOperator;
import fr.awildelephant.rdbms.engine.operators.InnerNestedLoopJoinOperator;
import fr.awildelephant.rdbms.engine.operators.JoinOperator;
import fr.awildelephant.rdbms.engine.operators.LimitOperator;
import fr.awildelephant.rdbms.engine.operators.MapOperator;
import fr.awildelephant.rdbms.engine.operators.ProjectionOperator;
import fr.awildelephant.rdbms.engine.operators.SortOperator;
import fr.awildelephant.rdbms.engine.operators.TableConstructorOperator;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.BreakdownLop;
import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.CollectLop;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LimitLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.LopVisitor;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.SortLop;
import fr.awildelephant.rdbms.plan.TableConstructorLop;
import fr.awildelephant.rdbms.plan.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.plan.arithmetic.Variable;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static fr.awildelephant.rdbms.engine.ValueExpressionToFormulaTransformer.createFormula;
import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;
import static java.util.stream.Collectors.toList;

public final class PlanExecutor implements LopVisitor<Stream<Table>> {

    private static final Logger LOGGER = LogManager.getLogger("Executor");

    private final Map<String, ManagedTable> tables;

    PlanExecutor(Map<String, ManagedTable> tables) {
        this.tables = tables;
    }

    @Override
    public Stream<Table> visit(AggregationLop aggregationNode) {
        final AggregationOperator operator = new AggregationOperator(aggregationNode.aggregates(),
                                                                     aggregationNode.schema());

        return apply(aggregationNode.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - AggregateOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - AggregateOperator - outputSize: {}", operatorId, output.numberOfTuples());

            return output;

        });
    }

    @Override
    public Stream<Table> visit(AliasLop aliasNode) {
        final AliasOperator operator = new AliasOperator(aliasNode.schema());

        return apply(aliasNode.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - RenameOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - RenameOperator - outputSize: {}", operatorId, output.numberOfTuples());

            return output;

        });
    }

    @Override
    public Stream<Table> visit(BaseTableLop baseTable) {
        final ManagedTable table = tables.get(baseTable.name());

        LOGGER.info("BaseTable - {} - size: {}", baseTable.name(), table.numberOfTuples());

        return Stream.of(table);
    }

    @Override
    public Stream<Table> visit(BreakdownLop breakdownNode) {
        final BreakdownOperator operator = new BreakdownOperator(breakdownNode.breakdowns());

        return apply(breakdownNode.input()).flatMap(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - BreakdownOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final List<Table> output = operator.compute(input).collect(toList());

            if (output.isEmpty()) {
                throw new UnsupportedOperationException("Grouping on an empty table is not supported");
            }

            final int minSize = output.stream().mapToInt(Table::numberOfTuples).min().getAsInt();
            final int maxSize = output.stream().mapToInt(Table::numberOfTuples).max().getAsInt();

            LOGGER.info("{} - BreakdownOperator - numberOfBuckets: {}, minSize: {}, maxSize {}", operatorId,
                        output.size(), minSize, maxSize);

            return output.stream();
        });
    }

    @Override
    public Stream<Table> visit(CartesianProductLop cartesianProductNode) {
        final CartesianProductOperator operator = new CartesianProductOperator(cartesianProductNode.schema());

        return apply(cartesianProductNode.leftInput())
                .flatMap(left -> apply(cartesianProductNode.rightInput())
                        .map(right -> operator.compute(left, right)));
    }

    @Override
    public Stream<Table> visit(CollectLop collectNode) {
        final List<Table> tables = apply(collectNode.input()).collect(toList());

        final int totalNumberOfTuples = tables.stream().mapToInt(Table::numberOfTuples).sum();

        final Table output = simpleTable(collectNode.schema(), totalNumberOfTuples);

        for (Table table : tables) {
            for (Record record : table) {
                output.add(record);
            }
        }

        return Stream.of(output);
    }

    @Override
    public Stream<Table> visit(DistinctLop distinctNode) {
        final DistinctOperator operator = new DistinctOperator();

        return apply(distinctNode.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - DistinctOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - DistinctOperator - outputSize: {}", operatorId, output.numberOfTuples());

            return output;

        });
    }

    @Override
    public Stream<Table> visit(FilterLop filter) {
        final FilterOperator operator = new FilterOperator(createFormula(filter.filter()));

        return apply(filter.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - FilterOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - FilterOperator - outputSize: {}", operatorId, output.numberOfTuples());

            return output;
        });
    }

    @Override
    public Stream<Table> visit(InnerJoinLop innerJoinLop) {
        final LogicalOperator leftInput = innerJoinLop.left();
        final LogicalOperator rightInput = innerJoinLop.right();

        final JoinOperator operator = chooseJoinOperator(innerJoinLop, leftInput.schema(), rightInput.schema());

        return apply(leftInput).flatMap(left -> apply(rightInput).map(right -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - InnerJoinOperator - leftSize: {}, rightSize: {}", operatorId, left.numberOfTuples(),
                        right.numberOfTuples());

            final Table output = operator.compute(left, right);

            LOGGER.info("{} - InnerJoinOperator - outputSize: {}", operatorId, output.numberOfTuples());

            return output;
        }));
    }

    private JoinOperator chooseJoinOperator(InnerJoinLop innerJoinLop, Schema leftInputSchema, Schema rightInputSchema) {
        final ValueExpression joinSpecification = innerJoinLop.joinSpecification();

        if (joinSpecification instanceof EqualExpression) {
            final EqualExpression equalExpression = (EqualExpression) joinSpecification;

            if (equalExpression.left() instanceof Variable && equalExpression.right() instanceof Variable) {
                final ColumnReference firstVariable = ((Variable) equalExpression.left()).name();
                final ColumnReference secondVariable = ((Variable) equalExpression.right()).name();

                final Schema outputSchema = innerJoinLop.schema();
                if (leftInputSchema.contains(firstVariable) && rightInputSchema.contains(secondVariable)) {
                    return new InnerHashJoinOperator(firstVariable, secondVariable, outputSchema);
                } else if (leftInputSchema.contains(secondVariable) && rightInputSchema.contains(firstVariable)) {
                    return new InnerHashJoinOperator(secondVariable, firstVariable, outputSchema);
                }
            }
        }

        return createNestedLoopJoinOperator(innerJoinLop, leftInputSchema, rightInputSchema);
    }

    private InnerNestedLoopJoinOperator createNestedLoopJoinOperator(InnerJoinLop innerJoinLop, Schema leftInputSchema, Schema rightInputSchema) {
        return new InnerNestedLoopJoinOperator(createFormula(innerJoinLop.joinSpecification()),
                                               leftInputSchema,
                                               rightInputSchema,
                                               innerJoinLop.schema());
    }

    @Override
    public Stream<Table> visit(LimitLop limitLop) {
        final LimitOperator operator = new LimitOperator(limitLop.limit());

        return apply(limitLop.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - LimitOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - LimitOperator - outputSize: {}", operatorId, output.numberOfTuples());

            return output;
        });
    }

    @Override
    public Stream<Table> visit(MapLop mapNode) {
        final List<Formula> formulas = mapNode.expressions()
                                              .stream()
                                              .map(ValueExpressionToFormulaTransformer::createFormula)
                                              .collect(toList());

        final MapOperator operator = new MapOperator(formulas, mapNode.schema());

        return apply(mapNode.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - MapOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - MapOperator - outputSize: {}", operatorId, output.numberOfTuples());

            return output;

        });
    }

    @Override
    public Stream<Table> visit(ProjectionLop projectionNode) {
        final ProjectionOperator operator = new ProjectionOperator(projectionNode.schema());

        return apply(projectionNode.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - ProjectOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - ProjectOperator - outputSize: {}", operatorId, output.numberOfTuples());

            return output;

        });
    }

    @Override
    public Stream<Table> visit(SortLop sortNode) {
        final SortOperator operator = new SortOperator(sortNode.schema(), sortNode.sortSpecificationList());

        return apply(sortNode.input()).map(input -> {
            final UUID operatorId = UUID.randomUUID();
            LOGGER.info("{} - SortOperator - inputSize: {}", operatorId, input.numberOfTuples());

            final Table output = operator.compute(input);

            LOGGER.info("{} - SortOperator - outputSize: {}", operatorId, output.numberOfTuples());

            return output;

        });
    }

    @Override
    public Stream<Table> visit(TableConstructorLop tableConstructor) {
        final List<List<Formula>> formulas = tableConstructor.matrix()
                                                             .stream()
                                                             .map(row -> row.stream()
                                                                            .map(ValueExpressionToFormulaTransformer::createFormula)
                                                                            .collect(toList()))
                                                             .collect(toList());

        final TableConstructorOperator operator = new TableConstructorOperator(formulas, tableConstructor.schema());

        LOGGER.info("TableConstructor");

        return Stream.of(operator.compute(null));
    }
}
