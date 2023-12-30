package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.UnaryOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;

public final class IsNullPredicate extends UnaryOperation<Operation> implements BooleanOperation {

    public IsNullPredicate(final Operation input) {
        super(input);
    }

    @Override
    public ThreeValuedLogic evaluateBoolean() {
        return child().evaluateAndWrap().isNull() ? TRUE : FALSE;
    }
}
