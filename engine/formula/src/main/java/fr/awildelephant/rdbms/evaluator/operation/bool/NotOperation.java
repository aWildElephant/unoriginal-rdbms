package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.UnaryOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

public final class NotOperation extends UnaryOperation<BooleanOperation> implements BooleanOperation {

    public NotOperation(final BooleanOperation input) {
        super(input);
    }

    @Override
    public ThreeValuedLogic evaluateBoolean() {
        return child().evaluateBoolean().negate();
    }
}
