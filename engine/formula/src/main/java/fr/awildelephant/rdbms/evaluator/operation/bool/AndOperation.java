package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.UNKNOWN;

public class AndOperation extends BinaryOperation<BooleanOperation, BooleanOperation> implements BooleanOperation {

    public AndOperation(final BooleanOperation left, final BooleanOperation right) {
        super(left, right);
    }

    @Override
    public ThreeValuedLogic evaluateBoolean() {
        final ThreeValuedLogic leftValue = leftChild().evaluateBoolean();
        return switch (leftValue) {
            case UNKNOWN -> UNKNOWN;
            case FALSE -> FALSE;
            case TRUE -> rightChild().evaluateBoolean();
        };
    }
}
