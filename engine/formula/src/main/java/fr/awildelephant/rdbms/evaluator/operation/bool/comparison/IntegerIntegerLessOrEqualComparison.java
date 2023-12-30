package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;

final class IntegerIntegerLessOrEqualComparison extends Comparison<Operation, Integer, Operation, Integer> {

    IntegerIntegerLessOrEqualComparison(final Operation left, final Operation right) {
        super(left, right, DomainValueUtils::extractInteger, DomainValueUtils::extractInteger);
    }

    @Override
    ThreeValuedLogic evaluateNotNull(final Integer leftInput, final Integer rightInput) {
        return leftInput <= rightInput ? TRUE : FALSE;
    }
}
