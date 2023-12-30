package fr.awildelephant.rdbms.evaluator.operation.date;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.time.LocalDate;
import java.util.function.Supplier;

public final class DateVariable extends LeafNode<Operation> implements DateOperation {

    private final Supplier<LocalDate> supplier;

    public DateVariable(final Supplier<LocalDate> supplier) {
        this.supplier = supplier;
    }

    @Override
    public LocalDate evaluateLocalDate() {
        return supplier.get();
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
