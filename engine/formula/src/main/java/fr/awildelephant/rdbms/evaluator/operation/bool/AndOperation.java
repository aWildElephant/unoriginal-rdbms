package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

public class AndOperation extends BinaryOperation<BooleanOperation, BooleanOperation> implements BooleanOperation {

    public AndOperation(final BooleanOperation left, final BooleanOperation right) {
        super(left, right);
    }

    @Override
    public ThreeValuedLogic evaluateBoolean() {
        return leftChild().evaluateBoolean().and(() -> rightChild().evaluateBoolean());
    }
}
