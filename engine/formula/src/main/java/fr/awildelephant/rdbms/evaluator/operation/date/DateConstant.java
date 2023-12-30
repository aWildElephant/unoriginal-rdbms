package fr.awildelephant.rdbms.evaluator.operation.date;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.time.LocalDate;

public final class DateConstant extends LeafNode<Operation> implements DateOperation {

    private final LocalDate value;

    public DateConstant(final LocalDate value) {
        this.value = value;
    }

    @Override
    public LocalDate evaluateLocalDate() {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
