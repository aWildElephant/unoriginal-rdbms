package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public class IntegerDivision implements Operation {

    private final Operation left;
    private final Operation right;

    private IntegerDivision(Operation left, Operation right) {
        this.left = left;
        this.right = right;
    }

    public static IntegerDivision integerDivision(Operation left, Operation right) {
        return new IntegerDivision(left, right);
    }

    @Override
    public DomainValue evaluate() {
        final DomainValue leftValue = left.evaluate();
        if (leftValue.isNull()) {
            return leftValue;
        }

        final int leftIntValue = leftValue.getInt();
        if (leftIntValue == 0) {
            return leftValue;
        }

        final DomainValue rightValue = right.evaluate();
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

    @Override
    public boolean isConstant() {
        return left.isConstant() && right.isConstant();
    }
}
