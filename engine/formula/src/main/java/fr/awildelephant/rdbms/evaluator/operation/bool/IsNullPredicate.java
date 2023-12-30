package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.UnaryOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;

public final class IsNullPredicate extends UnaryOperation<Operation> implements BooleanOperation {

    private IsNullPredicate(Operation input) {
        super(input);
    }

    public static IsNullPredicate isNullPredicate(Operation input) {
        return new IsNullPredicate(input);
    }

    @Override
    public ThreeValuedLogic evaluateBoolean() {
        return child().evaluateAndWrap().isNull() ? TRUE : FALSE;
    }
}
