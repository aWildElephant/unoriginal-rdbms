package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.UnaryOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.UNKNOWN;

public final class NotOperation extends UnaryOperation<BooleanOperation> implements BooleanOperation {

    public NotOperation(final BooleanOperation input) {
        super(input);
    }

    @Override
    public ThreeValuedLogic evaluateBoolean() {
        final ThreeValuedLogic childValue = child().evaluateBoolean();
        return switch (childValue) {
            case UNKNOWN -> UNKNOWN;
            case FALSE -> TRUE;
            case TRUE -> FALSE;
        };
    }
}
