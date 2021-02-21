package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class IsNullPredicate implements Operation {

    private final Operation input;

    private IsNullPredicate(Operation input) {
        this.input = input;
    }

    public static IsNullPredicate isNullPredicate(Operation input) {
        return new IsNullPredicate(input);
    }

    @Override
    public DomainValue evaluate() {
        return input.evaluate().isNull() ? trueValue() : falseValue();
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public boolean isConstant() {
        return input.isConstant();
    }
}
