package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;

public class DecimalAddition extends BinaryOperation<Operation, Operation> {

    private DecimalAddition(Operation left, Operation right) {
        super(left, right);
    }

    public static DecimalAddition decimalAddition(Operation left, Operation right) {
        return new DecimalAddition(left, right);
    }

    @Override
    public DomainValue evaluateAndWrap() {
        final DomainValue leftValue = leftChild().evaluateAndWrap();
        if (leftValue.isNull()) {
            return leftValue;
        }

        final DomainValue rightValue = rightChild().evaluateAndWrap();
        if (rightValue.isNull()) {
            return rightValue;
        }

        return decimalValue(leftValue.getBigDecimal().add(rightValue.getBigDecimal()));
    }

    @Override
    public Domain domain() {
        return DECIMAL;
    }
}
