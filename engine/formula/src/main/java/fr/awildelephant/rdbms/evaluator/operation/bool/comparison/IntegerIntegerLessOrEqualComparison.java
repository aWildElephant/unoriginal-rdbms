package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;

final class IntegerIntegerLessOrEqualComparison extends Comparison<IntegerOperation, Integer, IntegerOperation, Integer> {

    IntegerIntegerLessOrEqualComparison(final IntegerOperation left, final IntegerOperation right) {
        super(left, right, IntegerOperation::evaluateInteger, IntegerOperation::evaluateInteger);
    }

    @Override
    ThreeValuedLogic evaluateNotNull(final Integer leftInput, final Integer rightInput) {
        return leftInput <= rightInput ? TRUE : FALSE;
    }
}
