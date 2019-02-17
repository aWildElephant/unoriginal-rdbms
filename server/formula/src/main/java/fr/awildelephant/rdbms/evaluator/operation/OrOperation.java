package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public class OrOperation extends BinaryOperation {

    private OrOperation(Operation left, Operation right) {
        super(left, right);
    }

    public static OrOperation orOperation(Operation left, Operation right) {
        return new OrOperation(left, right);
    }

    @Override
    public DomainValue evaluate() {
        final DomainValue leftValue = left.evaluate();

        if (!leftValue.isNull() && leftValue.getBool()) {
            return trueValue();
        }

        final DomainValue rightValue = right.evaluate();

        if (rightValue.isNull() || leftValue.isNull()) {
            return nullValue();
        }

        return rightValue;
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }
}
