package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;

final class IntegerIntegerEqualComparison extends Comparison<Operation, Integer, Operation, Integer> {

    IntegerIntegerEqualComparison(final Operation left, final Operation right) {
        super(left, right, DomainValueUtils::extractInteger, DomainValueUtils::extractInteger);
    }

    @Override
    ThreeValuedLogic evaluateNotNull(Integer leftInput, Integer rightInput) {
        return leftInput.intValue() == rightInput.intValue() ? TRUE : FALSE;
    }
}
