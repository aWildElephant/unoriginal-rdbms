package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.tree.LeafNode;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

// TODO: abstract constant class
public final class BooleanConstant extends LeafNode<Operation> implements BooleanOperation {

    private final ThreeValuedLogic value;

    public BooleanConstant(final ThreeValuedLogic value) {
        this.value = value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public ThreeValuedLogic evaluate() {
        return value;
    }
}
