package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.engine.data.column.BooleanColumn;
import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.NewColumnBasedTable;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.execution.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.execution.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.execution.arithmetic.Variable;
import fr.awildelephant.rdbms.execution.arithmetic.function.VariableCollector;
import fr.awildelephant.rdbms.execution.arithmetic.visitor.NotNullValueExpressionVisitor;
import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.execution.operator.semijoin.ConstantMatcher;
import fr.awildelephant.rdbms.execution.operator.semijoin.HashSemiJoinMatcher;
import fr.awildelephant.rdbms.execution.operator.semijoin.NestedLoopSemiJoinMatcher;
import fr.awildelephant.rdbms.execution.operator.semijoin.SemiJoinMatcher;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.execution.JoinOutputSchemaFactory.innerJoinOutputSchema;
import static fr.awildelephant.rdbms.execution.filter.FilterExpander.expandFilters;
import static fr.awildelephant.rdbms.formula.creation.ValueExpressionToFormulaTransformer.createFormula;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;

public class SemiJoinOperator implements Operator {

    private final VariableCollector variableCollector = new VariableCollector();
    private final String leftInputKey;
    private final String rightInputKey;
    private final ValueExpression joinPredicate;
    private final Schema outputSchema;

    public SemiJoinOperator(String leftInputKey, String rightInputKey, ValueExpression joinPredicate, Schema outputSchema) {
        this.leftInputKey = leftInputKey;
        this.rightInputKey = rightInputKey;
        this.joinPredicate = joinPredicate;
        this.outputSchema = outputSchema;
    }

    @Override
    public Table compute(TemporaryStorage storage) {
        final Table leftInputTable = storage.get(leftInputKey);
        final Table rightInputTable = storage.get(rightInputKey);

        final Schema leftInputSchema = leftInputTable.schema();
        final Schema rightInputSchema = rightInputTable.schema();

        final SemiJoinMatcher matcher = createSemiJoinMatcher(leftInputSchema, rightInputSchema, joinPredicate, rightInputTable);

        final List<? extends Column> inputColumns = leftInputTable.columns();

        final List<Column> outputColumns = new ArrayList<>(inputColumns.size() + 1);
        outputColumns.addAll(inputColumns);
        final BooleanColumn resultColumn = new BooleanColumn(leftInputTable.numberOfTuples());
        outputColumns.add(resultColumn);

        for (Record record : leftInputTable) {
            final ThreeValuedLogic result = matcher.match(record);
            if (result == TRUE) {
                resultColumn.add(trueValue());
            } else if (result == FALSE) {
                resultColumn.add(falseValue());
            } else {
                resultColumn.add(nullValue());
            }
        }

        return new NewColumnBasedTable(outputSchema, outputColumns);
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

    @NotNull
    private ColumnReference variable(ValueExpression valueExpression) {
        final List<ColumnReference> variables = variableCollector.apply(valueExpression);
        if (variables.isEmpty()) {
            throw new IllegalStateException();
        }
        return variables.get(0);
    }
}
