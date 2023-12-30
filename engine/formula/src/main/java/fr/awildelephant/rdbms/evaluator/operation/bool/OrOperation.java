package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.UNKNOWN;

public class OrOperation extends BinaryOperation<BooleanOperation, BooleanOperation> implements BooleanOperation {

    private OrOperation(BooleanOperation left, BooleanOperation right) {
        super(left, right);
    }

    public static OrOperation orOperation(BooleanOperation left, BooleanOperation right) {
        return new OrOperation(left, right);
    }

    @Override
    public ThreeValuedLogic evaluate() {
        final ThreeValuedLogic leftValue = leftChild().evaluate();
        return switch (leftValue) {
            case UNKNOWN, FALSE -> switch (rightChild().evaluate()) {
                case UNKNOWN -> UNKNOWN;
                case FALSE -> leftValue;
                case TRUE -> TRUE;
            };
            case TRUE -> TRUE;
        };
    }
}
