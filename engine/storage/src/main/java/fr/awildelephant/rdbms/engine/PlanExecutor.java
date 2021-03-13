package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.table.ManagedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.AggregationOperator;
import fr.awildelephant.rdbms.engine.operators.AliasOperator;
import fr.awildelephant.rdbms.engine.operators.CartesianProductOperator;
import fr.awildelephant.rdbms.engine.operators.DistinctOperator;
import fr.awildelephant.rdbms.engine.operators.FilterOperator;
import fr.awildelephant.rdbms.engine.operators.InnerJoinOperator;
import fr.awildelephant.rdbms.engine.operators.JoinOperator;
import fr.awildelephant.rdbms.engine.operators.LimitOperator;
import fr.awildelephant.rdbms.engine.operators.MapOperator;
import fr.awildelephant.rdbms.engine.operators.ProjectionOperator;
import fr.awildelephant.rdbms.engine.operators.SortOperator;
import fr.awildelephant.rdbms.engine.operators.SubqueryExecutionOperator;
import fr.awildelephant.rdbms.engine.operators.TableConstructorOperator;
import fr.awildelephant.rdbms.engine.operators.join.HashJoinMatcher;
import fr.awildelephant.rdbms.engine.operators.join.JoinMatcher;
import fr.awildelephant.rdbms.engine.operators.join.JoinOutputCreator;
import fr.awildelephant.rdbms.engine.operators.join.LeftJoinOutputCreator;
import fr.awildelephant.rdbms.engine.operators.join.NestedLoopJoinMatcher;
import fr.awildelephant.rdbms.engine.operators.semijoin.HashSemiJoinMatcher;
import fr.awildelephant.rdbms.engine.operators.semijoin.NestedLoopSemiJoinMatcher;
import fr.awildelephant.rdbms.engine.operators.semijoin.SemiJoinMatcher;
import fr.awildelephant.rdbms.engine.operators.semijoin.SemiJoinOperator;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.DefaultLopVisitor;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LeftJoinLop;
import fr.awildelephant.rdbms.plan.LimitLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.LopVisitor;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.ScalarSubqueryLop;
import fr.awildelephant.rdbms.plan.SemiJoinLop;
import fr.awildelephant.rdbms.plan.SortLop;
import fr.awildelephant.rdbms.plan.DependentJoinLop;
import fr.awildelephant.rdbms.plan.TableConstructorLop;
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

import static fr.awildelephant.rdbms.formula.creation.ValueExpressionToFormulaTransformer.createFormula;
import static fr.awildelephant.rdbms.plan.arithmetic.FilterExpander.expandFilters;
import static fr.awildelephant.rdbms.schema.Schema.EMPTY_SCHEMA;
import static java.util.stream.Collectors.toList;

public final class PlanExecutor extends DefaultLopVisitor<Table> {

    private static final Logger LOGGER = LogManager.getLogger("Executor");

    private final Map<String, ManagedTable> tables;

    PlanExecutor(Map<String, ManagedTable> tables) {
        this.tables = tables;
    }

    @Override
    public Table visit(AggregationLop aggregationNode) {
        final Table inputTable = apply(aggregationNode.input());

        final UUID operatorId = UUID.randomUUID();

        final long inputSize = inputTable.numberOfTuples();

        LOGGER.info("{} - AggregateOperator - inputSize: {}", operatorId, inputSize);

        final AggregationOperator operator = new AggregationOperator(aggregationNode.aggregates(),
                                                                     aggregationNode.breakdowns(),
                                                                     aggregationNode.schema());

        final Table outputTable = operator.compute(inputTable);

        final long outputSize = outputTable.numberOfTuples();

        LOGGER.info("{} - AggregateOperator - outputSize: {}", operatorId, outputSize);

        return outputTable;
    }

    @Override
    public Table visit(AliasLop aliasNode) {
        final Table inputTable = apply(aliasNode.input());

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - RenameOperator - inputSize: {}", () -> operatorId, inputTable::numberOfTuples);

        final AliasOperator operator = new AliasOperator(aliasNode.schema());

        final Table outputTable = operator.compute(inputTable);

        LOGGER.info("{} - RenameOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(BaseTableLop baseTable) {
        final Table table = tables.get(baseTable.name());

        LOGGER.info("BaseTable - {} - outputSize: {}", baseTable::name, table::numberOfTuples);

        return table;
    }

    @Override
    public Table visit(CartesianProductLop cartesianProductNode) {
        final Table leftInput = apply(cartesianProductNode.leftInput());
        final Table rightInput = apply(cartesianProductNode.rightInput());

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - CartesianProductOperator - leftInputSize: {}, rightInputSize: {}", () -> operatorId,
                    leftInput::numberOfTuples, rightInput::numberOfTuples);

        final CartesianProductOperator operator = new CartesianProductOperator(cartesianProductNode.schema());

        final Table outputTable = operator.compute(leftInput, rightInput);

        LOGGER.info("{} - CartesianProductOperator - outputSize {}", () -> operatorId,
                    outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(DistinctLop distinctNode) {
        final Table inputTable = apply(distinctNode.input());

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - DistinctOperator - inputSize: {}", () -> operatorId, inputTable::numberOfTuples);

        final DistinctOperator operator = new DistinctOperator();
        final Table outputTable = operator.compute(inputTable);

        LOGGER.info("{} - DistinctOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(FilterLop filter) {
        final Table inputTable = apply(filter.input());

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - FilterOperator - inputSize: {}", () -> operatorId, inputTable::numberOfTuples);

        final FilterOperator operator = new FilterOperator(createFormula(filter.filter(), filter.input().schema()));

        final Table outputTable = operator.compute(inputTable);

        LOGGER.info("{} - FilterOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(InnerJoinLop innerJoinLop) {
        final LogicalOperator leftInput = innerJoinLop.left();
        final Table leftInputTable = apply(leftInput);

        final LogicalOperator rightInput = innerJoinLop.right();
        final Table rightInputTable = apply(rightInput);

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - InnerJoinOperator - leftSize: {}, rightSize: {}", () -> operatorId,
                    leftInputTable::numberOfTuples, rightInputTable::numberOfTuples);

        final Schema leftInputSchema = leftInputTable.schema();
        final Schema rightInputSchema = rightInput.schema();
        final Schema outputSchema = innerJoinLop.schema();
        final ValueExpression joinSpecification = innerJoinLop.joinSpecification();

        final Function<Table, JoinMatcher> matcherCreator = buildJoinMatcherCreator(leftInputSchema, rightInputSchema,
                                                                                    outputSchema, joinSpecification);

        final InnerJoinOperator operator = new InnerJoinOperator(matcherCreator, outputSchema);

        final Table outputTable = operator.compute(leftInputTable, rightInputTable);

        LOGGER.info("{} - InnerJoinOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(LeftJoinLop leftJoin) {
        final LogicalOperator leftInput = leftJoin.left();
        final Table leftInputTable = apply(leftInput);

        final LogicalOperator rightInput = leftJoin.right();
        final Table rightInputTable = apply(rightInput);

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - LeftJoinOperator - leftSize: {}, rightSize: {}", () -> operatorId,
                    leftInputTable::numberOfTuples,
                    rightInputTable::numberOfTuples);

        final Schema leftInputSchema = leftInput.schema();
        final Schema rightInputSchema = rightInput.schema();
        final Schema outputSchema = leftJoin.schema();
        final ValueExpression joinPredicate = leftJoin.joinSpecification();
        final JoinOutputCreator outputCreator = new LeftJoinOutputCreator(leftInputSchema, rightInputSchema);

        final Function<Table, JoinMatcher> matcherCreator = buildJoinMatcherCreator(leftInputSchema, rightInputSchema,
                                                                                    outputSchema, joinPredicate);

        final JoinMatcher matcher = matcherCreator.apply(rightInputTable);
        final JoinOperator operator = new JoinOperator(matcher, outputCreator, outputSchema);

        final Table outputTable = operator.compute(leftInputTable);

        LOGGER.info("{} - LeftJoinOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
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
    public Table visit(LimitLop limitLop) {
        final Table inputTable = apply(limitLop.input());

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - LimitOperator - inputSize: {}", () -> operatorId, inputTable::numberOfTuples);

        final LimitOperator operator = new LimitOperator(limitLop.limit());
        final Table outputTable = operator.compute(inputTable);

        LOGGER.info("{} - LimitOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(MapLop mapNode) {
        final Table inputTable = apply(mapNode.input());

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - MapOperator - inputSize: {}", () -> operatorId, inputTable::numberOfTuples);

        final Schema inputSchema = mapNode.input().schema();

        final List<Formula> formulas = mapNode.expressions()
                .stream()
                .map(expression -> createFormula(expression, inputSchema))
                .collect(toList());

        final MapOperator operator = new MapOperator(formulas, mapNode.schema());

        final Table outputTable = operator.compute(inputTable);

        LOGGER.info("{} - MapOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(ProjectionLop projectionNode) {
        final Table inputTable = apply(projectionNode.input());

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - ProjectOperator - inputSize: {}", () -> operatorId, inputTable::numberOfTuples);

        final ProjectionOperator operator = new ProjectionOperator(projectionNode.schema());

        final Table outputTable = operator.compute(inputTable);

        LOGGER.info("{} - ProjectOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(ScalarSubqueryLop scalarSubquery) {
        return apply(scalarSubquery.input());
//        final Table inputTable = apply(scalarSubquery.input());
//
//        final UUID operatorId = UUID.randomUUID();
//
//        LOGGER.info("{} - ScalarOperator - inputSize: {}", () -> operatorId, inputTable::numberOfTuples);
//
//        final Table outputTable = simpleTable(scalarSubquery.schema(), 1);
//
//        boolean foundOneRow = false;
//        for (Record record : inputTable) {
//            if (foundOneRow) {
//                throw new IllegalArgumentException("Scalar subquery cannot have more than one row");
//            }
//
//            foundOneRow = true;
//            outputTable.add(record);
//        }
//
//        LOGGER.info("{} - ScalarOperator - outputSize: 1", operatorId);
//
//        return outputTable;
    }

    @Override
    public Table visit(SemiJoinLop semiJoin) {
        final LogicalOperator leftInput = semiJoin.left();
        final Table leftInputTable = apply(leftInput);

        final LogicalOperator rightInput = semiJoin.right();
        final Table rightInputTable = apply(rightInput);

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - SemiJoinOperator - leftSize: {}, rightSize: {}", () -> operatorId,
                    leftInputTable::numberOfTuples, rightInputTable::numberOfTuples);

        final Schema leftInputSchema = leftInput.schema();
        final Schema rightInputSchema = rightInput.schema();

        final SemiJoinMatcher matcher = createSemiJoinMatcher(leftInputSchema, rightInputSchema,
                                                              semiJoin.predicate(), rightInputTable);
        final SemiJoinOperator operator = new SemiJoinOperator(semiJoin.schema(), matcher);

        final Table outputTable = operator.compute(leftInputTable);

        LOGGER.info("{} - SemiJoinOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    private SemiJoinMatcher createSemiJoinMatcher(Schema leftInputSchema,
                                                  Schema rightInputSchema,
                                                  ValueExpression joinPredicate,
                                                  Table rightTable) {
        final List<ValueExpression> expressions = expandFilters(joinPredicate);

        if (canUseHashJoin(expressions)) {
            return createHashSemiJoinMatcher(leftInputSchema, rightInputSchema, rightTable, expressions);
        } else {
            final Formula predicateFormula = createFormula(joinPredicate, leftInputSchema, rightInputSchema);
            return new NestedLoopSemiJoinMatcher(rightTable, predicateFormula);
        }
    }

    private SemiJoinMatcher createHashSemiJoinMatcher(Schema leftInputSchema, Schema rightInputSchema, Table rightTable,
                                                      List<ValueExpression> expressions) {
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
    public Table visit(SortLop sortNode) {
        final Table inputTable = apply(sortNode.input());

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - SortOperator - inputSize: {}", () -> operatorId, inputTable::numberOfTuples);

        final SortOperator operator = new SortOperator(sortNode.schema(), sortNode.sortSpecificationList());
        final Table output = operator.compute(inputTable);

        LOGGER.info("{} - SortOperator - outputSize: {}", () -> operatorId, output::numberOfTuples);

        return output;
    }

    @Override
    public Table visit(TableConstructorLop tableConstructor) {
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
        final Table outputTable = operator.compute(null);

        LOGGER.info("{} - TableConstructor - outputSize: {}", operatorId, numberOfRows);

        return outputTable;
    }

    @Override
    public Table defaultVisit(LogicalOperator operator) {
        throw new IllegalStateException("Unable to execute node " + operator);
    }
}
