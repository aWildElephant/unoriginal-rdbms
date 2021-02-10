package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.ManagedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.*;
import fr.awildelephant.rdbms.engine.operators.join.*;
import fr.awildelephant.rdbms.engine.operators.semijoin.HashSemiJoinMatcher;
import fr.awildelephant.rdbms.engine.operators.semijoin.SemiJoinMatcher;
import fr.awildelephant.rdbms.engine.operators.semijoin.SemiJoinOperator;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.plan.*;
import fr.awildelephant.rdbms.plan.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.plan.arithmetic.Variable;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static fr.awildelephant.rdbms.engine.ValueExpressionToFormulaTransformer.createFormula;
import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;
import static fr.awildelephant.rdbms.plan.arithmetic.FilterExpander.expandFilters;
import static fr.awildelephant.rdbms.schema.Schema.EMPTY_SCHEMA;
import static java.util.stream.Collectors.toList;

public final class PlanExecutor implements LopVisitor<List<Table>> {

    private static final Logger LOGGER = LogManager.getLogger("Executor");

    private final Map<String, ManagedTable> tables;

    PlanExecutor(Map<String, ManagedTable> tables) {
        this.tables = tables;
    }

    @Override
    public List<Table> visit(AggregationLop aggregationNode) {
        final List<Table> inputPartitions = apply(aggregationNode.input());

        final UUID operatorId = UUID.randomUUID();

        final long inputSize = computeSize(inputPartitions);

        LOGGER.info("{} - AggregateOperator - inputSize: {}", operatorId, inputSize);

        final AggregationOperator operator = new AggregationOperator(aggregationNode.aggregates(),
                aggregationNode.schema());

        final List<Table> outputPartitions = new ArrayList<>(inputPartitions.size());

        for (Table partition : inputPartitions) {
            outputPartitions.add(operator.compute(partition));
        }

        final long outputSize = computeSize(outputPartitions);

        LOGGER.info("{} - AggregateOperator - outputSize: {}", operatorId, outputSize);

        return outputPartitions;
    }

    @Override
    public List<Table> visit(AliasLop aliasNode) {
        final List<Table> inputPartitions = apply(aliasNode.input());

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - RenameOperator - inputSize: {}", () -> operatorId, () -> computeSize(inputPartitions));

        final AliasOperator operator = new AliasOperator(aliasNode.schema());

        final List<Table> outputPartitions = new ArrayList<>(inputPartitions.size());

        for (Table partition : inputPartitions) {
            outputPartitions.add(operator.compute(partition));
        }

        LOGGER.info("{} - RenameOperator - outputSize: {}", () -> operatorId, () -> computeSize(outputPartitions));

        return outputPartitions;
    }

    @Override
    public List<Table> visit(BaseTableLop baseTable) {
        final Table table = tables.get(baseTable.name());

        LOGGER.info("BaseTable - {} - outputSize: {}", baseTable.name(), table.numberOfTuples());

        return List.of(table);
    }

    @Override
    public List<Table> visit(BreakdownLop breakdownNode) {
        final List<Table> inputPartitions = apply(breakdownNode.input());

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - BreakdownOperator - inputSize: {}", () -> operatorId, () -> computeSize(inputPartitions));

        final BreakdownOperator operator = new BreakdownOperator(breakdownNode.breakdowns());

        final List<Table> outputPartitions = new ArrayList<>();

        for (Table partition : inputPartitions) {
            outputPartitions.addAll(operator.compute(partition));
        }

        LOGGER.info("{} - BreakdownOperator - outputSize {}, numberOfBuckets: {}", () -> operatorId,
                () -> computeSize(outputPartitions), outputPartitions::size);

        return outputPartitions;
    }

    @Override
    public List<Table> visit(CartesianProductLop cartesianProductNode) {
        final List<Table> leftPartitions = apply(cartesianProductNode.leftInput());
        final List<Table> rightPartitions = apply(cartesianProductNode.rightInput());

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - CartesianProductOperator - leftInputSize: {}, rightInputSize: {}", () -> operatorId,
                () -> computeSize(leftPartitions), () -> computeSize(rightPartitions));

        final CartesianProductOperator operator = new CartesianProductOperator(cartesianProductNode.schema());

        final List<Table> outputPartitions = new ArrayList<>(leftPartitions.size() * rightPartitions.size());

        for (Table leftPartition : leftPartitions) {
            for (Table rightPartition : rightPartitions) {
                outputPartitions.add(operator.compute(leftPartition, rightPartition));
            }
        }

        LOGGER.info("{} - CartesianProductOperator - outputSize {}", () -> operatorId,
                () -> computeSize(outputPartitions));

        return outputPartitions;
    }

    @Override
    public List<Table> visit(CollectLop collectNode) {
        final List<Table> inputPartitions = apply(collectNode.input());

        final UUID operatorId = UUID.randomUUID();
        final int numberOfRows = computeSize(inputPartitions);

        LOGGER.info("{} - MergeOperator - inputSize: {}", operatorId, numberOfRows);

        final Table output = collect(collectNode.schema(), inputPartitions);

        LOGGER.info("{} - MergeOperator - outputSize: {}", operatorId, numberOfRows);

        return List.of(output);
    }

    private Table collect(Schema schema, List<Table> inputPartitions) {
        if (inputPartitions.size() == 1) {
            return inputPartitions.get(0);
        }

        final int numberOfRows = computeSize(inputPartitions);
        final Table output = simpleTable(schema, numberOfRows);

        for (Table partition : inputPartitions) {
            for (Record record : partition) {
                output.add(record);
            }
        }
        return output;
    }

    @Override
    public List<Table> visit(DistinctLop distinctNode) {
        final List<Table> inputPartitions = apply(distinctNode.input());

        final int numberOfInputPartitions = inputPartitions.size();

        if (numberOfInputPartitions != 1) {
            throw new IllegalStateException(
                    "DistinctOperator must have exactly one input partition, got " + numberOfInputPartitions);
        }

        final Table input = inputPartitions.get(0);

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - DistinctOperator - inputSize: {}", () -> operatorId, input::numberOfTuples);

        final DistinctOperator operator = new DistinctOperator();
        final Table output = operator.compute(input);

        LOGGER.info("{} - DistinctOperator - outputSize: {}", () -> operatorId, output::numberOfTuples);

        return List.of(output);
    }

    @Override
    public List<Table> visit(FilterLop filter) {
        final List<Table> inputPartitions = apply(filter.input());

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - FilterOperator - inputSize: {}", () -> operatorId, () -> computeSize(inputPartitions));

        final FilterOperator operator = new FilterOperator(createFormula(filter.filter(), filter.input().schema()));

        final List<Table> outputPartitions = new ArrayList<>(inputPartitions.size());

        for (Table partition : inputPartitions) {
            outputPartitions.add(operator.compute(partition));
        }

        LOGGER.info("{} - FilterOperator - outputSize: {}", () -> operatorId, () -> computeSize(outputPartitions));

        return outputPartitions;
    }

    @Override
    public List<Table> visit(InnerJoinLop innerJoinLop) {
        final LogicalOperator leftInput = innerJoinLop.left();
        final List<Table> leftPartitions = apply(leftInput);

        final LogicalOperator rightInput = innerJoinLop.right();
        final List<Table> rightPartitions = apply(rightInput);

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - InnerJoinOperator - leftSize: {}, rightSize: {}", () -> operatorId,
                () -> computeSize(leftPartitions),
                () -> computeSize(rightPartitions));

        final Schema leftInputSchema = leftInput.schema();
        final Schema rightInputSchema = rightInput.schema();
        final Schema outputSchema = innerJoinLop.schema();
        final ValueExpression joinSpecification = innerJoinLop.joinSpecification();

        final Function<Table, JoinMatcher> matcherCreator = buildJoinMatcherCreator(leftInputSchema, rightInputSchema, outputSchema, joinSpecification);

        final InnerJoinOperator operator = new InnerJoinOperator(matcherCreator, outputSchema);

        final List<Table> outputPartitions = new ArrayList<>();
        for (Table leftPartition : leftPartitions) {
            for (Table rightPartition : rightPartitions) {
                outputPartitions.add(operator.compute(leftPartition, rightPartition));
            }
        }

        LOGGER.info("{} - InnerJoinOperator - outputSize: {}", () -> operatorId, () -> computeSize(outputPartitions));

        return outputPartitions;
    }

    @Override
    public List<Table> visit(LeftJoinLop leftJoin) {
        final LogicalOperator leftInput = leftJoin.left();
        final List<Table> leftPartitions = apply(leftInput);

        final LogicalOperator rightInput = leftJoin.right();
        final List<Table> rightPartitions = apply(rightInput);

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - LeftJoinOperator - leftSize: {}, rightSize: {}", () -> operatorId,
                () -> computeSize(leftPartitions),
                () -> computeSize(rightPartitions));

        final List<Table> outputPartitions = new ArrayList<>();

        final Schema leftInputSchema = leftInput.schema();
        final Schema rightInputSchema = rightInput.schema();
        final Schema outputSchema = leftJoin.schema();
        final ValueExpression joinPredicate = leftJoin.joinSpecification();
        final JoinOutputCreator outputCreator = new LeftJoinOutputCreator(leftInputSchema, rightInputSchema);

        final Function<Table, JoinMatcher> matcherCreator = buildJoinMatcherCreator(leftInputSchema, rightInputSchema, outputSchema, joinPredicate);

        for (Table leftPartition : leftPartitions) {
            for (Table rightPartition : rightPartitions) {
                final JoinMatcher matcher = matcherCreator.apply(rightPartition);

                outputPartitions.add(new JoinOperator(matcher, outputCreator, outputSchema).compute(leftPartition));
            }
        }

        LOGGER.info("{} - LeftJoinOperator - outputSize: {}", () -> operatorId, () -> computeSize(outputPartitions));

        return outputPartitions;
    }

    private Function<Table, JoinMatcher> buildJoinMatcherCreator(Schema leftInputSchema,
                                                                 Schema rightInputSchema,
                                                                 Schema outputSchema,
                                                                 ValueExpression joinPredicate) {
        final List<ValueExpression> expressions = expandFilters(joinPredicate);

        if (canUseHashJoin(expressions)) {
            return table -> createHashJoinMatcher(table, leftInputSchema, rightInputSchema, expressions);
        } else {
            return table -> createNestedLoopJoinMatcher(table, createFormula(joinPredicate, outputSchema));
        }
    }

    private boolean canUseHashJoin(List<ValueExpression> expressions) {
        for (ValueExpression expression : expressions) {
            if (!(expression instanceof EqualExpression)) {
                return false;
            }

            final EqualExpression equalExpression = (EqualExpression) expression;

            if (!(equalExpression.left() instanceof Variable) || !(equalExpression.right() instanceof Variable)) {
                return false;
            }
        }

        return true;
    }

    private JoinMatcher createHashJoinMatcher(Table rightTable,
                                              Schema leftInputSchema,
                                              Schema rightInputSchema,
                                              List<ValueExpression> joinSpecification) {
        final int numberOfEqualFilters = joinSpecification.size();

        final List<ColumnReference> leftJoinColumns = new ArrayList<>(numberOfEqualFilters);
        final List<ColumnReference> rightJoinColumns = new ArrayList<>(numberOfEqualFilters);

        for (ValueExpression expression : joinSpecification) {
            final EqualExpression equalExpression = (EqualExpression) expression;

            final ColumnReference equalLeftMember = equalExpression.left().variables().findAny().orElseThrow();
            final ColumnReference equalRightMember = equalExpression.right().variables().findAny().orElseThrow();

            if (leftInputSchema.contains(equalLeftMember)) {
                leftJoinColumns.add(equalLeftMember);
                rightJoinColumns.add(equalRightMember);
            } else {
                leftJoinColumns.add(equalRightMember);
                rightJoinColumns.add(equalLeftMember);
            }
        }

        final int[] leftMapping = new int[numberOfEqualFilters];
        final int[] rightMapping = new int[numberOfEqualFilters];

        for (int i = 0; i < numberOfEqualFilters; i++) {
            leftMapping[i] = leftInputSchema.indexOf(leftJoinColumns.get(i));
            rightMapping[i] = rightInputSchema.indexOf(rightJoinColumns.get(i));
        }

        return new HashJoinMatcher(rightTable, leftMapping, rightMapping);
    }

    private JoinMatcher createNestedLoopJoinMatcher(Table rightTable, Formula joinSpecification) {
        return new NestedLoopJoinMatcher(rightTable, joinSpecification);
    }

    @Override
    public List<Table> visit(LimitLop limitLop) {
        final List<Table> inputPartitions = apply(limitLop.input());

        final int numberOfInputPartitions = inputPartitions.size();

        if (numberOfInputPartitions != 1) {
            throw new IllegalStateException(
                    "LimitOperator must have exactly one input partition, got " + numberOfInputPartitions);
        }

        final Table input = inputPartitions.get(0);

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - LimitOperator - inputSize: {}", () -> operatorId, input::numberOfTuples);

        final LimitOperator operator = new LimitOperator(limitLop.limit());
        final Table output = operator.compute(input);

        LOGGER.info("{} - LimitOperator - outputSize: {}", () -> operatorId, output::numberOfTuples);

        return List.of(output);
    }

    @Override
    public List<Table> visit(MapLop mapNode) {
        final List<Table> inputPartitions = apply(mapNode.input());

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - MapOperator - inputSize: {}", () -> operatorId, () -> computeSize(inputPartitions));

        final Schema inputSchema = mapNode.input().schema();

        final List<Formula> formulas = mapNode.expressions()
                .stream()
                .map(expression -> createFormula(expression, inputSchema))
                .collect(toList());

        final MapOperator operator = new MapOperator(formulas, mapNode.schema());

        final List<Table> outputPartitions = new ArrayList<>(inputPartitions.size());

        for (Table partition : inputPartitions) {
            outputPartitions.add(operator.compute(partition));
        }

        LOGGER.info("{} - MapOperator - outputSize: {}", () -> operatorId, () -> computeSize(outputPartitions));

        return outputPartitions;
    }

    @Override
    public List<Table> visit(ProjectionLop projectionNode) {
        final List<Table> inputPartitions = apply(projectionNode.input());

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - ProjectOperator - inputSize: {}", () -> operatorId, () -> computeSize(inputPartitions));

        final ProjectionOperator operator = new ProjectionOperator(projectionNode.schema());

        final List<Table> outputPartitions = new ArrayList<>(inputPartitions.size());

        for (Table partition : inputPartitions) {
            outputPartitions.add(operator.compute(partition));
        }

        LOGGER.info("{} - ProjectOperator - outputSize: {}", () -> operatorId, () -> computeSize(outputPartitions));

        return outputPartitions;
    }

    @Override
    public List<Table> visit(ScalarSubqueryLop scalarSubquery) {
        final List<Table> inputPartitions = apply(scalarSubquery.input());

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - ScalarOperator - inputSize: {}", () -> operatorId, () -> computeSize(inputPartitions));

        final Table output = simpleTable(scalarSubquery.schema(), 1);

        boolean foundOneRow = false;
        for (Table table : inputPartitions) {
            for (Record record : table) {
                if (foundOneRow) {
                    throw new IllegalArgumentException("Scalar subquery cannot have more than one row");
                }

                foundOneRow = true;
                output.add(record);
            }
        }

        LOGGER.info("{} - ScalarOperator - outputSize: 1", operatorId);

        return List.of(output);
    }

    @Override
    public List<Table> visit(SemiJoinLop semiJoin) {
        final LogicalOperator leftInput = semiJoin.left();
        final List<Table> leftPartitions = apply(leftInput);

        final LogicalOperator rightInput = semiJoin.right();
        final List<Table> rightPartitions = apply(rightInput);

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - SemiJoinOperator - leftSize: {}, rightSize: {}", () -> operatorId,
                () -> computeSize(leftPartitions),
                () -> computeSize(rightPartitions));

        final List<Table> outputPartitions = new ArrayList<>();

        final Schema leftInputSchema = leftInput.schema();
        final Schema rightInputSchema = rightInput.schema();
        final Schema outputSchema = semiJoin.schema();

        final Table rightTable = collect(rightInput.schema(), rightPartitions);

        final SemiJoinMatcher matcher = createSemiJoinMatcher(leftInputSchema, rightInputSchema, outputSchema, semiJoin.predicate(), rightTable);
        final SemiJoinOperator operator = new SemiJoinOperator(semiJoin.schema(), matcher);

        for (Table leftPartition : leftPartitions) {
            outputPartitions.add(operator.compute(leftPartition));
        }

        LOGGER.info("{} - SemiJoinOperator - outputSize: {}", () -> operatorId, () -> computeSize(outputPartitions));

        return outputPartitions;
    }

    private SemiJoinMatcher createSemiJoinMatcher(Schema leftInputSchema,
                                                  Schema rightInputSchema,
                                                  Schema outputSchema,
                                                  ValueExpression joinPredicate,
                                                  Table rightTable) {
        final List<ValueExpression> expressions = expandFilters(joinPredicate);

        if (canUseHashJoin(expressions)) {
            return createHashSemiJoinMatcher(leftInputSchema, rightInputSchema, rightTable, expressions);
        } else {
            createFormula(joinPredicate, outputSchema);
            throw new UnsupportedOperationException();
        }
    }

    private SemiJoinMatcher createHashSemiJoinMatcher(Schema leftInputSchema, Schema rightInputSchema, Table rightTable, List<ValueExpression> expressions) {
        final int numberOfEqualFilters = expressions.size();

        final List<ColumnReference> leftJoinColumns = new ArrayList<>(numberOfEqualFilters);
        final List<ColumnReference> rightJoinColumns = new ArrayList<>(numberOfEqualFilters);

        for (ValueExpression expression : expressions) {
            final EqualExpression equalExpression = (EqualExpression) expression;

            final ColumnReference equalLeftMember = equalExpression.left().variables().findAny().orElseThrow();
            final ColumnReference equalRightMember = equalExpression.right().variables().findAny().orElseThrow();

            if (leftInputSchema.contains(equalLeftMember)) {
                leftJoinColumns.add(equalLeftMember);
                rightJoinColumns.add(equalRightMember);
            } else {
                leftJoinColumns.add(equalRightMember);
                rightJoinColumns.add(equalLeftMember);
            }
        }

        final int[] leftMapping = new int[numberOfEqualFilters];
        final int[] rightMapping = new int[numberOfEqualFilters];

        for (int i = 0; i < numberOfEqualFilters; i++) {
            leftMapping[i] = leftInputSchema.indexOf(leftJoinColumns.get(i));
            rightMapping[i] = rightInputSchema.indexOf(rightJoinColumns.get(i));
        }

        return new HashSemiJoinMatcher(rightTable, leftMapping, rightMapping);
    }

    @Override
    public List<Table> visit(SubqueryExecutionLop subqueryExecutionLop) {
        final List<Table> inputPartitions = apply(subqueryExecutionLop.input());

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - SubqueryOperator - inputSize: {}", () -> operatorId, () -> computeSize(inputPartitions));

        final SubqueryExecutionOperator operator = new SubqueryExecutionOperator(subqueryExecutionLop.subquery(),
                this,
                subqueryExecutionLop.schema());

        final List<Table> outputPartitions = new ArrayList<>(inputPartitions.size());

        for (Table partition : inputPartitions) {
            outputPartitions.add(operator.compute(partition));
        }

        LOGGER.info("{} - ScalarOperator - outputSize: {}", () -> operatorId, () -> computeSize(outputPartitions));

        return outputPartitions;
    }

    @Override
    public List<Table> visit(SortLop sortNode) {
        final List<Table> inputPartitions = apply(sortNode.input());

        final int numberOfInputPartitions = inputPartitions.size();

        if (numberOfInputPartitions != 1) {
            throw new IllegalStateException(
                    "SortOperator must have exactly one input partition, got " + numberOfInputPartitions);
        }
        final Table input = inputPartitions.get(0);

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - SortOperator - inputSize: {}", () -> operatorId, input::numberOfTuples);

        final SortOperator operator = new SortOperator(sortNode.schema(), sortNode.sortSpecificationList());
        final Table output = operator.compute(input);

        LOGGER.info("{} - SortOperator - outputSize: {}", () -> operatorId, output::numberOfTuples);

        return List.of(output);
    }

    @Override
    public List<Table> visit(TableConstructorLop tableConstructor) {
        final List<List<ValueExpression>> matrix = tableConstructor.matrix();

        final int numberOfRows = matrix.size();
        final int numberOfColumns;
        if (numberOfRows > 0) {
            numberOfColumns = matrix.get(0).size();
        } else {
            numberOfColumns = 0;
        }

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - TableConstructor - rows: {}, columns: {}", operatorId, numberOfRows, numberOfColumns);

        final List<List<Formula>> formulas = matrix.stream()
                .map(row -> row.stream()
                        .map(expression -> createFormula(expression,
                                EMPTY_SCHEMA))
                        .collect(toList()))
                .collect(toList());

        final TableConstructorOperator operator = new TableConstructorOperator(formulas, tableConstructor.schema());
        final Table output = operator.compute(null);

        LOGGER.info("{} - TableConstructor - outputSize: {}", operatorId, numberOfRows);

        return List.of(output);
    }

    private int computeSize(List<Table> inputPartitions) {
        return inputPartitions.stream().mapToInt(Table::numberOfTuples).sum();
    }
}
