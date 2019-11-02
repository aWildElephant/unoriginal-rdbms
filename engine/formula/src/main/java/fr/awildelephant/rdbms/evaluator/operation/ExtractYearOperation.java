package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public final class ExtractYearOperation implements Operation {

    private final Operation input;

    private ExtractYearOperation(Operation input) {
        this.input = input;
    }

    public static ExtractYearOperation extractYearOperation(Operation input) {
        return new ExtractYearOperation(input);
    }

    @Override
    public DomainValue evaluate() {
        final DomainValue date = input.evaluate();

        if (date.isNull()) {
            return nullValue();
        }

        return integerValue(date.getLocalDate().getYear());
    }

    @Override
    public Domain domain() {
        return INTEGER;
    }

    @Override
    public boolean isConstant() {
        return input.isConstant();
    }
}
