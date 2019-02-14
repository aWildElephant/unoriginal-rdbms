package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public final class IntegerAddition extends AbstractBinaryOperation {

    private IntegerAddition(Operation left, Operation right) {
        super(left, right);
    }

    public static IntegerAddition integerAddition(Operation left, Operation right) {
        return new IntegerAddition(left, right);
    }

    @Override
    public DomainValue evaluate() {
        final DomainValue leftValue = left.evaluate();
        if (leftValue.isNull()) {
            return nullValue();
        }

        final DomainValue rightValue = right.evaluate();
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
