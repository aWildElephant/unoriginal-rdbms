package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.DateValue.dateValue;
import static fr.awildelephant.rdbms.schema.Domain.DATE;

public class DateIntervalSubstraction extends BinaryOperation {

    private DateIntervalSubstraction(Operation left, Operation right) {
        super(left, right);
    }

    public static DateIntervalSubstraction dateIntervalSubstraction(Operation left, Operation right) {
        return new DateIntervalSubstraction(left, right);
    }

    @Override
    public DomainValue evaluate() {
        return dateValue(left.evaluate().getLocalDate().minus(right.evaluate().getPeriod()));
    }

    @Override
    public Domain domain() {
        return DATE;
    }
}
