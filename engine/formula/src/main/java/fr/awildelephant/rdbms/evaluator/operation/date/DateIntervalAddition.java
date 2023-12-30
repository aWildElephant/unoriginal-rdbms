package fr.awildelephant.rdbms.evaluator.operation.date;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;
import fr.awildelephant.rdbms.evaluator.operation.interval.IntervalOperation;

import java.time.LocalDate;
import java.time.Period;

public final class DateIntervalAddition extends BinaryOperation<DateOperation, IntervalOperation> implements DateOperation {

    public DateIntervalAddition(final DateOperation left, final IntervalOperation right) {
        super(left, right);
    }

    @Override
    public LocalDate evaluateLocalDate() {
        final LocalDate date = firstChild().evaluateLocalDate();

        if (date == null) {
            return null;
        }

        final Period period = secondChild().evaluatePeriod();

        if (period == null) {
            return null;
        }

        return date.plus(period);
    }
}
