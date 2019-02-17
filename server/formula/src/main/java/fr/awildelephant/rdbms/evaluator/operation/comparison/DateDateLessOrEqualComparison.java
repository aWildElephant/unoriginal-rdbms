package fr.awildelephant.rdbms.evaluator.operation.comparison;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;

class DateDateLessOrEqualComparison extends Comparison {

    DateDateLessOrEqualComparison(Operation left, Operation right) {
        super(left, right);
    }

    @Override
    boolean evaluateNotNull(DomainValue leftInput, DomainValue rightInput) {
        return !leftInput.getLocalDate().isAfter(rightInput.getLocalDate());
    }
}
