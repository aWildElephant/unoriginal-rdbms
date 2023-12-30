package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.UNKNOWN;

public class AndOperation extends BinaryOperation<BooleanOperation, BooleanOperation> implements BooleanOperation {

    private AndOperation(final BooleanOperation left, final BooleanOperation right) {
        super(left, right);
    }

    public static AndOperation andOperation(final BooleanOperation left, final BooleanOperation right) {
        return new AndOperation(left, right);
    }

    @Override
    public ThreeValuedLogic evaluate() {
        final ThreeValuedLogic leftValue = leftChild().evaluate();
        return switch (leftValue) {
            case UNKNOWN -> UNKNOWN;
            case FALSE -> FALSE;
            case TRUE -> rightChild().evaluate();
        };
    }
}
