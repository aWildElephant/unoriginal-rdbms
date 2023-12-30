package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.date.DateOperation;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.time.LocalDate;

public final class ExtractYearOperation extends UnaryNode<Operation, DateOperation> implements IntegerOperation {

    public ExtractYearOperation(final DateOperation input) {
        super(input);
    }

    @Override
    public Integer evaluateInteger() {
        final LocalDate date = child().evaluate();

        if (date == null) {
            return null;
        }

        return date.getYear();
    }

    @Override
    public boolean isConstant() {
        return child().isConstant();
    }
}
