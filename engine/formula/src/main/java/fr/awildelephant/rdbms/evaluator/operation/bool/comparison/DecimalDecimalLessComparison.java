package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import java.math.BigDecimal;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;

final class DecimalDecimalLessComparison extends Comparison<Operation, BigDecimal, Operation, BigDecimal> {

    DecimalDecimalLessComparison(final Operation left, final Operation right) {
        super(left, right, DomainValueUtils::extractBigDecimal, DomainValueUtils::extractBigDecimal);
    }

    @Override
    ThreeValuedLogic evaluateNotNull(final BigDecimal leftInput, final BigDecimal rightInput) {
        return leftInput.compareTo(rightInput) < 0 ? TRUE : FALSE;
    }
}
