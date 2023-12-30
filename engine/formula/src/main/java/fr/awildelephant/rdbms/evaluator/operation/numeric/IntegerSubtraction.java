package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public final class IntegerSubtraction extends BinaryOperation<IntegerOperation, IntegerOperation> implements IntegerOperation {

    public IntegerSubtraction(final IntegerOperation left, final IntegerOperation right) {
        super(left, right);
    }

    @Override
    public DomainValue evaluateAndWrap() {
        final DomainValue leftValue = leftChild().evaluateAndWrap();
        if (leftValue.isNull()) {
            return nullValue();
        }

        final DomainValue rightValue = rightChild().evaluateAndWrap();
        if (rightValue.isNull()) {
            return nullValue();
        }

        return integerValue(leftValue.getInt() - rightValue.getInt());
    }

    @Override
    public Integer evaluate() {
        final Integer leftValue = leftChild().evaluate();
        if (leftValue == null) {
            return null;
        }

        final Integer rightValue = rightChild().evaluate();
        if (rightValue == null) {
            return null;
        }

        return leftValue - rightValue;
    }
}
