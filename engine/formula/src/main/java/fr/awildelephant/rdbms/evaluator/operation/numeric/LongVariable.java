package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.util.function.Supplier;

public final class LongVariable extends LeafNode<Operation> implements LongOperation {

    private final Supplier<Long> supplier;

    public LongVariable(final Supplier<Long> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Long evaluateLong() {
        return supplier.get();
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
