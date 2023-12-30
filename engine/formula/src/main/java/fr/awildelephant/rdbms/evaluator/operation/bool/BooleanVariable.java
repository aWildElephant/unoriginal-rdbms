package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import java.util.function.Supplier;

public final class BooleanVariable extends LeafNode<Operation> implements BooleanOperation {

    private final Supplier<ThreeValuedLogic> supplier;

    public BooleanVariable(final Supplier<ThreeValuedLogic> supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public ThreeValuedLogic evaluate() {
        return supplier.get();
    }
}
