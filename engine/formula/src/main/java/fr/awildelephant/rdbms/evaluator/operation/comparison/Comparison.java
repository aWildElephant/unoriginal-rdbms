package fr.awildelephant.rdbms.evaluator.operation.comparison;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public abstract class Comparison extends BinaryOperation {

    Comparison(Operation left, Operation right) {
        super(left, right);
    }

    @Override
    public DomainValue evaluate() {
        final DomainValue leftInput = left.evaluate();

        if (leftInput.isNull()) {
            return nullValue();
        }

        final DomainValue rightInput = right.evaluate();

        if (rightInput.isNull()) {
            return nullValue();
        }

        return evaluateNotNull(leftInput, rightInput) ? trueValue() : falseValue();
    }

    abstract boolean evaluateNotNull(DomainValue leftInput, DomainValue rightInput);

    @Override
    public Domain domain() {
        return BOOLEAN;
    }
}
