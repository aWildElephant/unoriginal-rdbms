package fr.awildelephant.rdbms.evaluator.operation.date;

import fr.awildelephant.rdbms.evaluator.operation.ConstantOperation;

import java.time.LocalDate;

public final class DateConstant extends ConstantOperation implements DateOperation {

    private final LocalDate value;

    public DateConstant(final LocalDate value) {
        this.value = value;
    }

    @Override
    public LocalDate evaluateLocalDate() {
        return value;
    }
}
