package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.evaluator.operation.date.DateOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import java.time.LocalDate;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;

final class DateDateLessComparison extends Comparison<DateOperation, LocalDate, DateOperation, LocalDate> {

    DateDateLessComparison(DateOperation left, DateOperation right) {
        super(left, right, DateOperation::evaluateLocalDate, DateOperation::evaluateLocalDate);
    }

    @Override
    ThreeValuedLogic evaluateNotNull(LocalDate leftInput, LocalDate rightInput) {
        return leftInput.isBefore(rightInput) ? TRUE : FALSE;
    }
}
