package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.DateValue.dateValue;
import static fr.awildelephant.rdbms.schema.Domain.DATE;

public class DateIntervalAddition extends BinaryOperation {

    private DateIntervalAddition(Operation left, Operation right) {
        super(left, right);
    }

    public static DateIntervalAddition dateIntervalAddition(Operation left, Operation right) {
        return new DateIntervalAddition(left, right);
    }

    @Override
    public DomainValue evaluate() {
        return dateValue(left.evaluate().getLocalDate().plus(right.evaluate().getPeriod()));
    }

    @Override
    public Domain domain() {
        return DATE;
    }
}
