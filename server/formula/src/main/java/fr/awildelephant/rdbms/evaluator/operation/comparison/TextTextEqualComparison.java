package fr.awildelephant.rdbms.evaluator.operation.comparison;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;

public class TextTextEqualComparison extends Comparison {

    private final Operation left;
    private final Operation right;

    TextTextEqualComparison(Operation left, Operation right) {
        this.left = left;
        this.right = right;
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

        return leftInput.getString().equals(rightInput.getString()) ? trueValue() : falseValue();
    }

    @Override
    public boolean isConstant() {
        return left.isConstant() && right.isConstant();
    }
}
