package fr.awildelephant.rdbms.evaluator.operation.interval;

import fr.awildelephant.rdbms.evaluator.operation.ConstantOperation;

import java.time.Period;

public final class IntervalConstant extends ConstantOperation implements IntervalOperation {

    private final Period value;

    public IntervalConstant(final Period value) {
        this.value = value;
    }

    @Override
    public Period evaluatePeriod() {
        return value;
    }
}
