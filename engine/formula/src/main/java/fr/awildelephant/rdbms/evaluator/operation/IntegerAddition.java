package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public final class IntegerAddition extends BinaryOperation<Operation, Operation> {

    private IntegerAddition(Operation left, Operation right) {
        super(left, right);
    }

    public static IntegerAddition integerAddition(Operation left, Operation right) {
        return new IntegerAddition(left, right);
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

        return integerValue(leftValue.getInt() + rightValue.getInt());
    }

    @Override
    public Domain domain() {
        return INTEGER;
    }
}
