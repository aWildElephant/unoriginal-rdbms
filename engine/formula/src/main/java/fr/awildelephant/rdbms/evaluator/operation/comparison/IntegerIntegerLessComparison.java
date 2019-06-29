package fr.awildelephant.rdbms.evaluator.operation.comparison;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;

class IntegerIntegerLessComparison extends Comparison {

    IntegerIntegerLessComparison(Operation left, Operation right) {
        super(left, right);
    }

    @Override
    boolean evaluateNotNull(DomainValue leftInput, DomainValue rightInput) {
        return leftInput.getInt() < rightInput.getInt();
    }
}
