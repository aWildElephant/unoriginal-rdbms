package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.evaluator.operation.text.TextOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;

final class TextTextEqualComparison extends Comparison<TextOperation, String, TextOperation, String> {

    TextTextEqualComparison(final TextOperation left, final TextOperation right) {
        super(left, right, TextOperation::evaluateString, TextOperation::evaluateString);
    }

    @Override
    ThreeValuedLogic evaluateNotNull(final String leftInput, final String rightInput) {
        return leftInput.equals(rightInput) ? TRUE : FALSE;
    }
}
