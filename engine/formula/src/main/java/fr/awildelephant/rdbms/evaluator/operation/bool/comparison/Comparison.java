package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.bool.BooleanOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import java.util.function.Function;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.UNKNOWN;

public abstract class Comparison<L extends Operation, LV, R extends Operation, RV> extends BinaryOperation<L, R> implements BooleanOperation {

    private final Function<L, LV> leftEvaluation;
    private final Function<R, RV> rightEvaluation;

    Comparison(final L left, final R right, Function<L, LV> leftEvaluation, Function<R, RV> rightEvaluation) {
        super(left, right);
        this.leftEvaluation = leftEvaluation;
        this.rightEvaluation = rightEvaluation;
    }

    @Override
    public ThreeValuedLogic evaluateBoolean() {
        final LV leftValue = leftEvaluation.apply(leftChild());
        if (leftValue == null) {
            return UNKNOWN;
        }

        final RV rightValue = rightEvaluation.apply(rightChild());
        if (rightValue == null) {
            return UNKNOWN;
        }

        return evaluateNotNull(leftValue, rightValue);
    }

    abstract ThreeValuedLogic evaluateNotNull(final LV leftInput, final RV rightInput);
}
