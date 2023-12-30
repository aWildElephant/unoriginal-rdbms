package fr.awildelephant.rdbms.evaluator.operation.date;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;
import fr.awildelephant.rdbms.evaluator.operation.interval.IntervalOperation;

import java.time.LocalDate;
import java.time.Period;

public final class DateIntervalSubstraction extends BinaryOperation<DateOperation, IntervalOperation> implements DateOperation {

    public DateIntervalSubstraction(final DateOperation left, final IntervalOperation right) {
        super(left, right);
    }

    @Override
    public LocalDate evaluate() {
        final LocalDate date = firstChild().evaluate();

        if (date == null) {
            return null;
        }

        final Period period = secondChild().evaluate();

        if (period == null) {
            return null;
        }

        return date.minus(period);
    }
}
