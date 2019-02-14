package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;

public class DecimalAddition extends AbstractBinaryOperation {

    private DecimalAddition(Operation left, Operation right) {
        super(left, right);
    }

    public static DecimalAddition decimalAddition(Operation left, Operation right) {
        return new DecimalAddition(left, right);
    }

    @Override
    public DomainValue evaluate() {
        final DomainValue leftValue = left.evaluate();
        if (leftValue.isNull()) {
            return leftValue;
        }

        final DomainValue rightValue = right.evaluate();
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
