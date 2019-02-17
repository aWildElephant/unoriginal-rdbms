package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;

public class DecimalSubtraction extends BinaryOperation {

    private DecimalSubtraction(Operation left, Operation right) {
        super(left, right);
    }

    public static DecimalSubtraction decimalSubtraction(Operation left, Operation right) {
        return new DecimalSubtraction(left, right);
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

        return decimalValue(leftValue.getBigDecimal().subtract(rightValue.getBigDecimal()));
    }

    @Override
    public Domain domain() {
        return DECIMAL;
    }
}
