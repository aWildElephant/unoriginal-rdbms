package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.DefaultLopVisitor;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.awildelephant.rdbms.plan.arithmetic.ExpressionHelper.alwaysTrue;
import static fr.awildelephant.rdbms.plan.arithmetic.FilterCollapser.collapseFilters;
import static java.util.stream.Collectors.toList;

public final class JoinReordering extends DefaultLopVisitor<LogicalOperator> {

    @Override
    public LogicalOperator visit(CartesianProductLop cartesianProduct) {
        return reorderMultiJoin(new JoinSplitter().apply(cartesianProduct));
    }

    @Override
    public LogicalOperator visit(InnerJoinLop innerJoin) {
        return reorderMultiJoin(new JoinSplitter().apply(innerJoin));
    }

    private LogicalOperator reorderMultiJoin(MultiJoin multiJoin) {
        final List<LogicalOperator> inputs = new ArrayList<>(multiJoin.operators());

        final ValueExpression alwaysTrue = alwaysTrue();

        final List<ValueExpression> filters = multiJoin.expressions().stream()
                .filter(f -> !alwaysTrue.equals(f))
                .collect(toList());

        if (filters.isEmpty()) {
            return cartesianProducts(inputs);
        }

        final ValueExpression filter = filters.remove(filters.size() - 1);

        final List<LogicalOperator> inputsToJoin = inputsToHaveAllColumns(inputs, filter);

        inputs.removeAll(inputsToJoin);

        final LogicalOperator leftInput = apply(inputsToJoin.get(0));
        final LogicalOperator rightInput = cartesianProducts(listExcludingFirstElement(inputsToJoin));
        final LogicalOperator leftmostInput = new InnerJoinLop(leftInput, rightInput, filter,
                joinOutputSchema(leftInput.schema(),
                        rightInput.schema()));

        return continueReordering(leftmostInput, inputs, filters);
    }

    private LogicalOperator continueReordering(LogicalOperator leftmostInput, List<LogicalOperator> remainingInputs, List<ValueExpression> remainingFilters) {
        if (remainingInputs.isEmpty()) {
            final Optional<ValueExpression> filter = collapseFilters(remainingFilters);

            if (filter.isPresent()) {
                return new FilterLop(leftmostInput, filter.get());
            } else {
                return leftmostInput;
            }
        }

        final Optional<ValueExpression> applicableFilter = getAnyApplicableFilter(remainingFilters, leftmostInput);

        if (applicableFilter.isEmpty()) {
            final LogicalOperator right = remainingInputs.get(0);
            final Schema joinOutputSchema = joinOutputSchema(leftmostInput.schema(), right.schema());
            final CartesianProductLop join = new CartesianProductLop(leftmostInput, right, joinOutputSchema);

            return continueReordering(join, listExcludingFirstElement(remainingInputs), remainingFilters);
        }

        final ValueExpression filter = applicableFilter.get();
        remainingFilters.remove(filter);

        final List<LogicalOperator> inputsToJoin = inputsToHaveAllColumns(remainingInputs, filter);

        if (inputsToJoin.isEmpty()) {
            return continueReordering(new FilterLop(leftmostInput, filter), remainingInputs, remainingFilters);
        }

        remainingInputs.removeAll(inputsToJoin);

        final LogicalOperator right = cartesianProducts(inputsToJoin);
        final Schema joinOutputSchema = joinOutputSchema(leftmostInput.schema(), right.schema());
        final InnerJoinLop join = new InnerJoinLop(leftmostInput, right, filter, joinOutputSchema);

        return continueReordering(join, remainingInputs, remainingFilters);
    }

    private List<LogicalOperator> listExcludingFirstElement(List<LogicalOperator> remainingInputs) {
        return remainingInputs.subList(1, remainingInputs.size());
    }

    private Optional<ValueExpression> getAnyApplicableFilter(List<ValueExpression> filters, LogicalOperator operator) {
        final Schema schema = operator.schema();

        return filters.stream()
                .filter(filter -> filter.variables().anyMatch(schema::contains))
                .findAny();
    }

    private List<LogicalOperator> inputsToHaveAllColumns(List<LogicalOperator> inputs, ValueExpression filter) {
        final List<ColumnReference> columns = filter.variables().collect(toList());

        return inputs.stream()
                .filter(input -> providesAnyColumn(columns, input.schema()))
                .collect(toList());
    }

    private boolean providesAnyColumn(List<ColumnReference> columns, Schema inputSchema) {
        for (ColumnReference column : columns) {
            if (inputSchema.contains(column)) {
                return true;
            }
        }

        return false;
    }

    private LogicalOperator cartesianProducts(List<LogicalOperator> inputs) {
        LogicalOperator output = apply(inputs.get(0));

        for (int i = 1; i < inputs.size(); i++) {
            final LogicalOperator right = inputs.get(i);
            output = new CartesianProductLop(output, right, joinOutputSchema(output.schema(), right.schema()));
        }

        return output;
    }

    private static Schema joinOutputSchema(Schema leftSchema, Schema rightSchema) {
        return leftSchema.extend(rightSchema.columnMetadataList());
    }

    @Override
    public LogicalOperator defaultVisit(LogicalOperator operator) {
        return operator.transformInputs(this);
    }
}
