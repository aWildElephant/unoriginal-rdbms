package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

public final class OrOperation extends BinaryOperation<BooleanOperation, BooleanOperation> implements BooleanOperation {

    public OrOperation(final BooleanOperation left, final BooleanOperation right) {
        super(left, right);
    }

    @Override
    public ThreeValuedLogic evaluateBoolean() {
        return leftChild().evaluateBoolean().or(() -> rightChild().evaluateBoolean());
    }
}
