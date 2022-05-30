package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.table.ManagedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.operators.AggregationOperator;
import fr.awildelephant.rdbms.engine.operators.AliasOperator;
import fr.awildelephant.rdbms.engine.operators.CartesianProductOperator;
import fr.awildelephant.rdbms.engine.operators.DistinctOperator;
import fr.awildelephant.rdbms.engine.operators.FilterOperator;
import fr.awildelephant.rdbms.engine.operators.JoinOperator;
import fr.awildelephant.rdbms.engine.operators.LimitOperator;
import fr.awildelephant.rdbms.engine.operators.MapOperator;
import fr.awildelephant.rdbms.engine.operators.ProjectionOperator;
import fr.awildelephant.rdbms.engine.operators.SortOperator;
import fr.awildelephant.rdbms.engine.operators.TableConstructorOperator;
import fr.awildelephant.rdbms.engine.operators.join.FlippedInnerJoinOutputCreator;
import fr.awildelephant.rdbms.engine.operators.join.HashJoinMatcher;
import fr.awildelephant.rdbms.engine.operators.join.InnerJoinOutputCreator;
import fr.awildelephant.rdbms.engine.operators.join.JoinMatcher;
import fr.awildelephant.rdbms.engine.operators.join.JoinOutputCreator;
import fr.awildelephant.rdbms.engine.operators.join.LeftJoinOutputCreator;
import fr.awildelephant.rdbms.engine.operators.join.NestedLoopJoinMatcher;
import fr.awildelephant.rdbms.engine.operators.semijoin.ConstantMatcher;
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
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.ScalarSubqueryLop;
import fr.awildelephant.rdbms.plan.SemiJoinLop;
import fr.awildelephant.rdbms.plan.SortLop;
import fr.awildelephant.rdbms.plan.TableConstructorLop;
import fr.awildelephant.rdbms.plan.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.plan.arithmetic.Variable;
import fr.awildelephant.rdbms.plan.arithmetic.function.VariableCollector;
import fr.awildelephant.rdbms.plan.arithmetic.visitor.NotNullValueExpressionVisitor;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static fr.awildelephant.rdbms.formula.creation.ValueExpressionToFormulaTransformer.createFormula;
import static fr.awildelephant.rdbms.plan.JoinOutputSchemaFactory.innerJoinOutputSchema;
import static fr.awildelephant.rdbms.plan.filter.FilterExpander.expandFilters;
import static fr.awildelephant.rdbms.schema.Schema.EMPTY_SCHEMA;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;
import static java.util.stream.Collectors.toList;

// TODO: move to rdbms-database package, then will probably be removed after execution refactoring
public final class PlanExecutor extends DefaultLopVisitor<Table> {

    private static final Logger LOGGER = LogManager.getLogger("Executor");

    private final VariableCollector variableCollector = new VariableCollector();
    private final Map<String, ManagedTable> tables;

    public PlanExecutor(Map<String, ManagedTable> tables) {
        this.tables = tables;
    }

    @Override
    public Table visit(AggregationLop aggregation) {
        final Table inputTable = apply(aggregation.input());

        final UUID operatorId = UUID.randomUUID();

        final long inputSize = inputTable.numberOfTuples();

        LOGGER.info("{} - AggregateOperator - inputSize: {}", operatorId, inputSize);

        final AggregationOperator operator = new AggregationOperator(aggregation.aggregates(),
                aggregation.breakdowns(),
                aggregation.schema());

        final Table outputTable = operator.compute(inputTable);

        final long outputSize = outputTable.numberOfTuples();

        LOGGER.info("{} - AggregateOperator - outputSize: {}", operatorId, outputSize);

        return outputTable;
    }

    @Override
    public Table visit(AliasLop aliasNode) {
        final Table inputTable = apply(aliasNode.input());

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - AliasOperator - inputSize: {}", () -> operatorId, inputTable::numberOfTuples);

        final AliasOperator operator = new AliasOperator(aliasNode.schema());

        final Table outputTable = operator.compute(inputTable);

        LOGGER.info("{} - AliasOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(BaseTableLop baseTable) {
        final Table table = tables.get(baseTable.name());

        LOGGER.info("BaseTable - {} - outputSize: {}", baseTable::name, table::numberOfTuples);

        return table;
    }

    @Override
    public Table visit(CartesianProductLop cartesianProduct) {
        final Table leftInputTable = apply(cartesianProduct.left());
        final Table rightInputTable = apply(cartesianProduct.right());

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - CartesianProductOperator - leftInputSize: {}, rightInputSize: {}", () -> operatorId,
                leftInputTable::numberOfTuples, rightInputTable::numberOfTuples);

        final CartesianProductOperator operator = new CartesianProductOperator(cartesianProduct.schema());

        final Table outputTable = operator.compute(leftInputTable, rightInputTable);

        LOGGER.info("{} - CartesianProductOperator - outputSize {}", () -> operatorId,
                outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(DistinctLop distinct) {
        final Table inputTable = apply(distinct.input());

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

        final FilterOperator operator = new FilterOperator(createFormula(filter.filter(), inputTable.schema()));

        final Table outputTable = operator.compute(inputTable);

        LOGGER.info("{} - FilterOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(InnerJoinLop innerJoin) {
        final Table leftInputTable = apply(innerJoin.left());
        final Table rightInputTable = apply(innerJoin.right());

        final UUID operatorId = UUID.randomUUID();

        final int leftInputSize = leftInputTable.numberOfTuples();
        final int rightInputSize = rightInputTable.numberOfTuples();

        LOGGER.info("{} - InnerJoinOperator - leftSize: {}, rightSize: {}", operatorId, leftInputSize, rightInputSize);

        final Schema leftInputSchema = leftInputTable.schema();
        final Schema rightInputSchema = rightInputTable.schema();
        final Schema outputSchema = innerJoin.schema();

        final Table outputTable;
        if (rightInputSize > leftInputSize) {
            final Function<Table, JoinMatcher> matcherCreator = buildJoinMatcherCreator(rightInputSchema,
                    leftInputSchema,
                    innerJoin.joinSpecification());

            final JoinMatcher matcher = matcherCreator.apply(leftInputTable);

            final JoinOperator operator = new JoinOperator(matcher, new FlippedInnerJoinOutputCreator(), outputSchema);

            outputTable = operator.compute(rightInputTable);
        } else {
            final Function<Table, JoinMatcher> matcherCreator = buildJoinMatcherCreator(leftInputSchema,
                    rightInputSchema,
                    innerJoin.joinSpecification());

            final JoinMatcher matcher = matcherCreator.apply(rightInputTable);

            final JoinOperator operator = new JoinOperator(matcher, new InnerJoinOutputCreator(), outputSchema);

            outputTable = operator.compute(leftInputTable);
        }

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
                joinPredicate);

        final JoinMatcher matcher = matcherCreator.apply(rightInputTable);
        final JoinOperator operator = new JoinOperator(matcher, outputCreator, outputSchema);

        final Table outputTable = operator.compute(leftInputTable);

        LOGGER.info("{} - LeftJoinOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    private Function<Table, JoinMatcher> buildJoinMatcherCreator(Schema leftInputSchema,
                                                                 Schema rightInputSchema,
                                                                 ValueExpression joinPredicate) {
        final List<ValueExpression> expressions = expandFilters(joinPredicate);

        final Schema joinInputSchema = innerJoinOutputSchema(leftInputSchema, rightInputSchema);

        if (canUseHashJoin(expressions, joinInputSchema)) {
            return table -> createHashJoinMatcher(table, leftInputSchema, rightInputSchema, expressions);
        } else {
            return table -> createNestedLoopJoinMatcher(table, createFormula(joinPredicate, leftInputSchema,
                    rightInputSchema));
        }
    }

    private boolean canUseHashJoin(List<ValueExpression> expressions, Schema joinInputSchema) {
        final NotNullValueExpressionVisitor ensureNotNull = new NotNullValueExpressionVisitor(joinInputSchema);

        for (ValueExpression expression : expressions) {
            if (!Boolean.TRUE.equals(ensureNotNull.apply(expression))) {
                return false;
            }

            if (!(expression instanceof final EqualExpression equalExpression)) {
                return false;
            }

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

            final ColumnReference equalLeftMember = variable(equalExpression.left());
            final ColumnReference equalRightMember = variable(equalExpression.right());

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

    @NotNull
    private ColumnReference variable(ValueExpression valueExpression) {
        final List<ColumnReference> variables = variableCollector.apply(valueExpression);
        if (variables.isEmpty()) {
            throw new IllegalStateException();
        }
        return variables.get(0);
    }

    private JoinMatcher createNestedLoopJoinMatcher(Table rightTable, Formula joinSpecification) {
        return new NestedLoopJoinMatcher(rightTable, joinSpecification);
    }

    @Override
    public Table visit(LimitLop limit) {
        final Table inputTable = apply(limit.input());

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - LimitOperator - inputSize: {}", () -> operatorId, inputTable::numberOfTuples);

        final LimitOperator operator = new LimitOperator(limit.limit());
        final Table outputTable = operator.compute(inputTable);

        LOGGER.info("{} - LimitOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(MapLop map) {
        final Table inputTable = apply(map.input());

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - MapOperator - inputSize: {}", () -> operatorId, inputTable::numberOfTuples);

        final Schema inputSchema = inputTable.schema();

        final List<Formula> formulas = map.expressions()
                .stream()
                .map(expression -> createFormula(expression, inputSchema))
                .collect(toList());

        final MapOperator operator = new MapOperator(formulas, map.schema());

        final Table outputTable = operator.compute(inputTable);

        LOGGER.info("{} - MapOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(ProjectionLop projection) {
        final Table inputTable = apply(projection.input());

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - ProjectOperator - inputSize: {}", () -> operatorId, inputTable::numberOfTuples);

        final ProjectionOperator operator = new ProjectionOperator(projection.schema());

        final Table outputTable = operator.compute(inputTable);

        LOGGER.info("{} - ProjectOperator - outputSize: {}", () -> operatorId, outputTable::numberOfTuples);

        return outputTable;
    }

    @Override
    public Table visit(ScalarSubqueryLop scalarSubquery) {
        return apply(scalarSubquery.input());
        // TODO: scalar subquery operator not implemented but still there
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
        final Table leftInputTable = apply(semiJoin.left());
        final Table rightInputTable = apply(semiJoin.right());

        final UUID operatorId = UUID.randomUUID();

        LOGGER.info("{} - SemiJoinOperator - leftSize: {}, rightSize: {}", () -> operatorId,
                leftInputTable::numberOfTuples, rightInputTable::numberOfTuples);

        final Schema leftInputSchema = leftInputTable.schema();
        final Schema rightInputSchema = rightInputTable.schema();

        final SemiJoinMatcher matcher = createSemiJoinMatcher(leftInputSchema, rightInputSchema, semiJoin.predicate(),
                rightInputTable);

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

        final Schema joinInputSchema = innerJoinOutputSchema(leftInputSchema, rightInputSchema);

        if (canUseHashJoin(expressions, joinInputSchema)) {
            return createHashSemiJoinMatcher(leftInputSchema, rightInputSchema, rightTable, expressions);
        } else if (joinPredicate != null) {
            final Formula predicateFormula = createFormula(joinPredicate, leftInputSchema, rightInputSchema);
            return new NestedLoopSemiJoinMatcher(rightTable, predicateFormula);
        } else {
            return new ConstantMatcher(rightTable.isEmpty() ? FALSE : TRUE);
        }
    }

    private SemiJoinMatcher createHashSemiJoinMatcher(Schema leftInputSchema, Schema rightInputSchema, Table rightTable,
                                                      List<ValueExpression> expressions) {
        final int numberOfEqualFilters = expressions.size();

        final List<ColumnReference> leftJoinColumns = new ArrayList<>(numberOfEqualFilters);
        final List<ColumnReference> rightJoinColumns = new ArrayList<>(numberOfEqualFilters);

        for (ValueExpression expression : expressions) {
            final EqualExpression equalExpression = (EqualExpression) expression;

            final ColumnReference equalLeftMember = variable(equalExpression.left());
            final ColumnReference equalRightMember = variable(equalExpression.right());

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
    public Table visit(SortLop sort) {
        final Table inputTable = apply(sort.input());

        final UUID operatorId = UUID.randomUUID();
        LOGGER.info("{} - SortOperator - inputSize: {}", () -> operatorId, inputTable::numberOfTuples);

        final SortOperator operator = new SortOperator(sort.schema(), sort.sortSpecificationList());
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
