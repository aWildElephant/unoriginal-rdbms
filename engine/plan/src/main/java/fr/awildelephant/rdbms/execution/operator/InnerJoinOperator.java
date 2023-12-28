package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.arithmetic.Variable;
import fr.awildelephant.rdbms.arithmetic.visitor.NotNullValueExpressionVisitor;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.execution.operator.join.FlippedInnerJoinOutputCreator;
import fr.awildelephant.rdbms.execution.operator.join.HashJoinMatcher;
import fr.awildelephant.rdbms.execution.operator.join.InnerJoinOutputCreator;
import fr.awildelephant.rdbms.execution.operator.join.JoinMatcher;
import fr.awildelephant.rdbms.execution.operator.join.NestedLoopJoinMatcher;
import fr.awildelephant.rdbms.function.VariableCollector;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.storage.data.table.Table;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static fr.awildelephant.rdbms.arithmetic.optimization.FilterExpander.expandFilters;
import static fr.awildelephant.rdbms.execution.JoinOutputSchemaFactory.innerJoinOutputSchema;
import static fr.awildelephant.rdbms.formula.creation.ValueExpressionToFormulaTransformer.createFormula;

public final class InnerJoinOperator implements Operator {

    private final VariableCollector variableCollector = new VariableCollector();
    private final String leftInputKey;
    private final String rightInputKey;
    private final Schema outputSchema;
    private final ValueExpression joinSpecification;

    public InnerJoinOperator(String leftInputKey, String rightInputKey, Schema outputSchema, ValueExpression joinSpecification) {
        this.leftInputKey = leftInputKey;
        this.rightInputKey = rightInputKey;
        this.outputSchema = outputSchema;
        this.joinSpecification = joinSpecification;
    }

    @Override
    public Table compute(TemporaryStorage storage) {
        final Table leftInputTable = storage.get(leftInputKey);
        final Table rightInputTable = storage.get(rightInputKey);

        final int leftInputSize = leftInputTable.numberOfTuples();
        final int rightInputSize = rightInputTable.numberOfTuples();

        final Schema leftInputSchema = leftInputTable.schema();
        final Schema rightInputSchema = rightInputTable.schema();

        final Table outputTable;
        if (rightInputSize > leftInputSize) {
            final Function<Table, JoinMatcher> matcherCreator = buildJoinMatcherCreator(rightInputSchema,
                    leftInputSchema,
                    joinSpecification);

            final JoinMatcher matcher = matcherCreator.apply(leftInputTable);

            final JoinOperator operator = new JoinOperator(matcher, new FlippedInnerJoinOutputCreator(), outputSchema);

            outputTable = operator.compute(rightInputTable);
        } else {
            final Function<Table, JoinMatcher> matcherCreator = buildJoinMatcherCreator(leftInputSchema,
                    rightInputSchema,
                    joinSpecification);

            final JoinMatcher matcher = matcherCreator.apply(rightInputTable);

            final JoinOperator operator = new JoinOperator(matcher, new InnerJoinOutputCreator(), outputSchema);

            outputTable = operator.compute(leftInputTable);
        }

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
        return variables.getFirst();
    }

    private JoinMatcher createNestedLoopJoinMatcher(Table rightTable, Formula joinSpecification) {
        return new NestedLoopJoinMatcher(rightTable, joinSpecification);
    }
}
