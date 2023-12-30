package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.ConstantOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

public final class BooleanConstant extends ConstantOperation implements BooleanOperation {

    private final ThreeValuedLogic value;

    public BooleanConstant(final ThreeValuedLogic value) {
        this.value = value;
    }

    @Override
    public ThreeValuedLogic evaluateBoolean() {
        return value;
    }
}
