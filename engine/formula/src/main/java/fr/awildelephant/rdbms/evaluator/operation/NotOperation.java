package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public class NotOperation implements Operation {

    private final Operation input;

    private NotOperation(Operation input) {
        this.input = input;
    }

    public static NotOperation notOperation(Operation input) {
        return new NotOperation(input);
    }

    @Override
    public DomainValue evaluate() {
        final DomainValue inputValue = input.evaluate();

        if (inputValue.isNull()) {
            return nullValue();
        }

        return inputValue.getBool() ? falseValue() : trueValue();
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
