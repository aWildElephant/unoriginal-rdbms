package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.evaluator.operation.numeric.DecimalOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import java.math.BigDecimal;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;

final class DecimalLessOrEqualComparison extends Comparison<DecimalOperation, BigDecimal, DecimalOperation, BigDecimal> {

    DecimalLessOrEqualComparison(final DecimalOperation left, final DecimalOperation right) {
        super(left, right, DecimalOperation::evaluateBigDecimal, DecimalOperation::evaluateBigDecimal);
    }

    @Override
    ThreeValuedLogic evaluateNotNull(final BigDecimal leftInput, final BigDecimal rightInput) {
        return leftInput.compareTo(rightInput) <= 0 ? TRUE : FALSE;
    }
}
