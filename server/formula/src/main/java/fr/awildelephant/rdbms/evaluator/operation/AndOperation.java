package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public class AndOperation extends AbstractBinaryOperation {

    private AndOperation(Operation left, Operation right) {
        super(left, right);
    }

    public static AndOperation andOperation(Operation left, Operation right) {
        return new AndOperation(left, right);
    }

    @Override
    public DomainValue evaluate() {
        final DomainValue leftValue = left.evaluate();

        if (leftValue.isNull()) {
            return nullValue();
        }

        if (!leftValue.getBool()) {
            return falseValue();
        }

        return right.evaluate();
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }
}
