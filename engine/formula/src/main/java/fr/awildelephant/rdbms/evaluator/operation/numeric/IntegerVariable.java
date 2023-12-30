package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.util.function.Supplier;

public final class IntegerVariable extends LeafNode<Operation> implements IntegerOperation {

    private final Supplier<Integer> supplier;

    public IntegerVariable(final Supplier<Integer> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Integer evaluate() {
        return supplier.get();
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
