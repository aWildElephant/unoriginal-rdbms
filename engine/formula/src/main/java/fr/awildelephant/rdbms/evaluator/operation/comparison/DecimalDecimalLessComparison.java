package fr.awildelephant.rdbms.evaluator.operation.comparison;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;

class DecimalDecimalLessComparison extends Comparison {

    DecimalDecimalLessComparison(Operation left, Operation right) {
        super(left, right);
    }

    @Override
    boolean evaluateNotNull(DomainValue leftInput, DomainValue rightInput) {
        return leftInput.getBigDecimal().compareTo(rightInput.getBigDecimal()) < 0;
    }
}
