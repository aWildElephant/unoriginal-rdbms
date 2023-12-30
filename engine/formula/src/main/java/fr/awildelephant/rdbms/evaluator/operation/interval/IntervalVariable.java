package fr.awildelephant.rdbms.evaluator.operation.interval;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.time.Period;
import java.util.function.Supplier;

public final class IntervalVariable extends LeafNode<Operation> implements IntervalOperation {

    private final Supplier<Period> supplier;

    public IntervalVariable(final Supplier<Period> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Period evaluate() {
        return supplier.get();
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
