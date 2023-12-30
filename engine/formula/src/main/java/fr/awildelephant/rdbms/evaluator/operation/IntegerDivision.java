package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public class IntegerDivision extends BinaryOperation<Operation, Operation> {

    private IntegerDivision(Operation left, Operation right) {
        super(left, right);
    }

    public static IntegerDivision integerDivision(Operation left, Operation right) {
        return new IntegerDivision(left, right);
    }

    @Override
    public DomainValue evaluateAndWrap() {
        final DomainValue leftValue = leftChild().evaluateAndWrap();
        if (leftValue.isNull()) {
            return leftValue;
        }

        final int leftIntValue = leftValue.getInt();
        if (leftIntValue == 0) {
            return leftValue;
        }

        final DomainValue rightValue = rightChild().evaluateAndWrap();
        if (rightValue.isNull()) {
            return nullValue();
        }

        final int rightIntValue = rightValue.getInt();
        if (rightIntValue == 0) {
            throw new IllegalStateException("Division by zero");
        }

        return integerValue(leftIntValue / rightIntValue);
    }

    @Override
    public Domain domain() {
        return INTEGER;
    }
}
