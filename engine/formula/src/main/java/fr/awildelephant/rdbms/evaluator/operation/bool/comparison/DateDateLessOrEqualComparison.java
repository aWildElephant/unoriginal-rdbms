package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.evaluator.operation.date.DateOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import java.time.LocalDate;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;

final class DateDateLessOrEqualComparison extends Comparison<DateOperation, LocalDate, DateOperation, LocalDate> {

    DateDateLessOrEqualComparison(final DateOperation left, final DateOperation right) {
        super(left, right, DateOperation::evaluate, DateOperation::evaluate);
    }

    @Override
    ThreeValuedLogic evaluateNotNull(final LocalDate leftInput, final LocalDate rightInput) {
        return !leftInput.isAfter(rightInput) ? TRUE : FALSE;
    }
}
